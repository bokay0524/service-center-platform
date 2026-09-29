import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api'

const statuses = ['RECEIVED','INSPECTING','REPAIRING','WAITING_PARTS','COMPLETED','SHIPPED','CANCELLED']

export default function RequestDetailPage() {
  const { id } = useParams()
  const [request, setRequest] = useState(null)
  const [integration, setIntegration] = useState('')
  const [error, setError] = useState('')
  const role = localStorage.getItem('role')

  const load = () => api.get(`/requests/${id}`)
    .then(r => setRequest(r.data))
    .catch(() => setError('접수 상세 정보를 불러오지 못했습니다.'))

  useEffect(() => { load() }, [id])

  if (error) return <div className="panel error">{error}</div>
  if (!request) return <div className="panel">Loading...</div>

  const changeStatus = async (status) => {
    setError('')
    try {
      await api.patch(`/requests/${id}/status`, { status })
      await load()
    } catch (e) {
      setError(e.response?.data?.message || '상태 변경에 실패했습니다.')
    }
  }

  const call = async (type, method='post') => {
    setError('')
    try {
      const response = await api[method](`/integrations/${id}/${type}`)
      setIntegration(response.data.result)
    } catch (e) {
      setError(e.response?.status === 403 ? 'ADMIN 권한이 필요한 기능입니다.' : '외부 연동 호출에 실패했습니다.')
    }
  }

  return (
    <>
      <div className="pageTitle"><div><Link className="back" to="/requests">← Requests</Link><h1>{request.referenceNo}</h1></div></div>
      {error && <div className="error pageError">{error}</div>}
      <div className="detailGrid">
        <section className="panel">
          <div className="eyebrow">CUSTOMER</div><h2>{request.customerName}</h2>
          <dl><dt>Phone</dt><dd>{request.customerPhone}</dd><dt>Email</dt><dd>{request.customerEmail || '-'}</dd><dt>Address</dt><dd>{request.customerAddress || '-'}</dd><dt>Branch</dt><dd>{request.branchCode}</dd></dl>
        </section>
        <section className="panel">
          <div className="eyebrow">WORKFLOW</div><h2>{request.status}</h2>
          <select value={request.status} onChange={e => changeStatus(e.target.value)}>{statuses.map(s => <option key={s}>{s}</option>)}</select>
          <div className="buttonStack">
            <button onClick={() => call('notification')}>Mock Notification</button>
            {role === 'ADMIN' && <button className="secondary" onClick={() => call('erp')}>Mock ERP Transfer</button>}
            <button className="secondary" onClick={() => call('delivery','get')}>Mock Delivery Status</button>
          </div>
          {role !== 'ADMIN' && <p className="hint">ERP transfer is restricted to ADMIN role.</p>}
          {integration && <div className="result">{integration}</div>}
        </section>
      </div>
      <section className="panel tablePanel">
        <div className="panelHeader"><div><div className="eyebrow">PRODUCTS</div><h2>Service Items</h2></div></div>
        <table><thead><tr><th>Category</th><th>Model</th><th>Serial</th><th>Symptom</th><th>Status</th></tr></thead><tbody>
          {request.items.map(i => <tr key={i.id}><td>{i.productCategory}</td><td>{i.modelName}</td><td>{i.serialNo || '-'}</td><td>{i.symptom}</td><td><span className="badge">{i.status}</span></td></tr>)}
        </tbody></table>
      </section>
    </>
  )
}
