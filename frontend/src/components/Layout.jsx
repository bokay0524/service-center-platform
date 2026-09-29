import { useEffect, useState } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { api } from '../api'

export default function Layout() {
  const navigate = useNavigate()
  const [me, setMe] = useState({ username: '-', role: localStorage.getItem('role') || '-' })

  useEffect(() => {
    api.get('/auth/me')
      .then(({ data }) => {
        setMe(data)
        localStorage.setItem('role', data.role)
      })
      .catch(() => {})
  }, [])

  const logout = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('role')
    navigate('/login')
  }

  return (
    <div className="shell">
      <aside className="sidebar">
        <div className="brand">SC<span>Platform</span></div>
        <p className="eyebrow">PORTFOLIO PROJECT</p>
        <nav>
          <NavLink to="/dashboard">Dashboard</NavLink>
          <NavLink to="/requests">Service Requests</NavLink>
        </nav>
        <div className="sidebarFooter">
          <div className="role">User: {me.username}</div>
          <div className="role">Role: {me.role}</div>
          <button className="secondary full" onClick={logout}>Logout</button>
        </div>
      </aside>
      <main className="main">
        <header className="topbar">
          <div>
            <div className="eyebrow">SERVICE OPERATIONS</div>
            <strong>Service Center Management</strong>
          </div>
          <div className="statusDot"><span /> API Connected</div>
        </header>
        <div className="content"><Outlet /></div>
      </main>
    </div>
  )
}
