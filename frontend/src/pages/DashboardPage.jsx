import { useEffect, useState } from 'react'
import { api } from '../api'

const labels = {
  TOTAL: '전체 접수', RECEIVED: '접수', INSPECTING: '점검', REPAIRING: '수리 중',
  WAITING_PARTS: '부품 대기', COMPLETED: '완료', SHIPPED: '배송', CANCELLED: '취소'
}

export default function DashboardPage() {
  const [counts, setCounts] = useState({})
  useEffect(() => { api.get('/dashboard').then(r => setCounts(r.data)) }, [])

  return (
    <>
      <div className="pageTitle"><div><div className="eyebrow">OVERVIEW</div><h1>Dashboard</h1></div></div>
      <div className="metricGrid">
        {Object.entries(labels).map(([key, label]) => (
          <article className={key === 'TOTAL' ? 'metric metricPrimary' : 'metric'} key={key}>
            <span>{label}</span><strong>{counts[key] ?? 0}</strong><small>{key}</small>
          </article>
        ))}
      </div>
      <section className="panel">
        <div className="panelHeader"><div><div className="eyebrow">ARCHITECTURE NOTE</div><h2>Why this project exists</h2></div></div>
        <p className="muted">실무에서 경험한 업무 흐름을 회사 고유 코드와 분리해 새롭게 모델링했습니다. 외부 연동은 Adapter로 추상화하고, 인증은 Spring Security + JWT로 통합했습니다.</p>
      </section>
    </>
  )
}
