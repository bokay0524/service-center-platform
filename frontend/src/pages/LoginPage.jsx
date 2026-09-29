import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../api'

export default function LoginPage() {
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('admin1234')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const { data } = await api.post('/auth/login', { username, password })
      localStorage.setItem('accessToken', data.accessToken)
      localStorage.setItem('role', data.role)
      navigate('/dashboard')
    } catch {
      setError('로그인에 실패했습니다. Demo 계정을 확인하세요.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="loginPage">
      <section className="loginIntro">
        <div className="brand large">SC<span>Platform</span></div>
        <h1>Service operations,<br />modeled for the web.</h1>
        <p>접수 · 수리 · 배송 · ERP 연동 흐름을 일반화한 공개용 포트폴리오 프로젝트</p>
        <div className="techRow"><span>Spring Boot</span><span>React</span><span>JPA</span><span>JWT</span></div>
      </section>
      <form className="loginCard" onSubmit={submit}>
        <div className="eyebrow">DEMO ACCESS</div>
        <h2>Sign in</h2>
        <label>Username<input value={username} onChange={e => setUsername(e.target.value)} /></label>
        <label>Password<input type="password" value={password} onChange={e => setPassword(e.target.value)} /></label>
        {error && <div className="error">{error}</div>}
        <button disabled={loading}>{loading ? 'Signing in...' : 'Sign in'}</button>
        <p className="hint">ADMIN: admin / admin1234<br />CENTER: center / center1234</p>
      </form>
    </div>
  )
}
