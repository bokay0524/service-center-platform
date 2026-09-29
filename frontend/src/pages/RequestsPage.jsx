import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'

const emptyItem = () => ({
  productCategory: 'Audio', modelName: '', serialNo: '', symptom: ''
})

const emptyForm = () => ({
  customerName: '', customerPhone: '', customerEmail: '', customerAddress: '', branchCode: 'SEOUL-01',
  items: [emptyItem()]
})

export default function RequestsPage() {
  const [requests, setRequests] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [show, setShow] = useState(false)
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  const load = () => api.get('/requests')
    .then(r => setRequests(r.data))
    .catch(() => setError('접수 목록을 불러오지 못했습니다.'))

  useEffect(() => { load() }, [])

  const updateItem = (index, field, value) => {
    setForm(prev => ({
      ...prev,
      items: prev.items.map((item, i) => i === index ? { ...item, [field]: value } : item)
    }))
  }

  const addItem = () => setForm(prev => ({ ...prev, items: [...prev.items, emptyItem()] }))
  const removeItem = (index) => setForm(prev => ({
    ...prev,
    items: prev.items.filter((_, i) => i !== index)
  }))

  const create = async (e) => {
    e.preventDefault()
    setError('')
    setSaving(true)
    try {
      await api.post('/requests', form)
      setForm(emptyForm())
      setShow(false)
      await load()
    } catch (e) {
      setError(e.response?.data?.message || '접수 등록에 실패했습니다.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <>
      <div className="pageTitle">
        <div><div className="eyebrow">OPERATIONS</div><h1>Service Requests</h1></div>
        <button onClick={() => setShow(!show)}>{show ? 'Close' : '+ New Request'}</button>
      </div>

      {error && <div className="error pageError">{error}</div>}

      {show && <form className="panel formGrid" onSubmit={create}>
        <label>Customer name<input required value={form.customerName} onChange={e => setForm({...form, customerName:e.target.value})} /></label>
        <label>Phone<input required value={form.customerPhone} onChange={e => setForm({...form, customerPhone:e.target.value})} /></label>
        <label>Email<input type="email" value={form.customerEmail} onChange={e => setForm({...form, customerEmail:e.target.value})} /></label>
        <label>Branch<input required value={form.branchCode} onChange={e => setForm({...form, branchCode:e.target.value})} /></label>
        <label className="wide">Address<input value={form.customerAddress} onChange={e => setForm({...form, customerAddress:e.target.value})} /></label>

        <div className="wide itemSectionHeader">
          <div><div className="eyebrow">MASTER-DETAIL</div><h2>Service Items</h2></div>
          <button type="button" className="secondary" onClick={addItem}>+ Add item</button>
        </div>

        {form.items.map((item, index) => (
          <div className="wide itemEditor" key={index}>
            <div className="itemEditorTitle">
              <strong>Item {index + 1}</strong>
              {form.items.length > 1 && <button type="button" className="textButton" onClick={() => removeItem(index)}>Remove</button>}
            </div>
            <div className="itemEditorGrid">
              <label>Category<input required value={item.productCategory} onChange={e => updateItem(index, 'productCategory', e.target.value)} /></label>
              <label>Model<input required value={item.modelName} onChange={e => updateItem(index, 'modelName', e.target.value)} /></label>
              <label>Serial<input value={item.serialNo} onChange={e => updateItem(index, 'serialNo', e.target.value)} /></label>
              <label className="wide">Symptom<textarea required value={item.symptom} onChange={e => updateItem(index, 'symptom', e.target.value)} /></label>
            </div>
          </div>
        ))}

        <div className="wide"><button disabled={saving} type="submit">{saving ? 'Creating...' : 'Create service request'}</button></div>
      </form>}

      <section className="panel tablePanel">
        <table>
          <thead><tr><th>Reference</th><th>Customer</th><th>Branch</th><th>Status</th><th>Items</th><th>Created</th></tr></thead>
          <tbody>
            {requests.map(r => <tr key={r.id}>
              <td><Link to={`/requests/${r.id}`}>{r.referenceNo}</Link></td>
              <td>{r.customerName}</td><td>{r.branchCode}</td>
              <td><span className="badge">{r.status}</span></td><td>{r.items.length}</td>
              <td>{new Date(r.createdAt).toLocaleString()}</td>
            </tr>)}
            {requests.length === 0 && <tr><td colSpan="6" className="emptyCell">No service requests.</td></tr>}
          </tbody>
        </table>
      </section>
    </>
  )
}
