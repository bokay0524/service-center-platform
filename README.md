# Service Center Platform

기업 서비스센터 업무 시스템 개발 경험을 바탕으로 **포트폴리오 목적으로 새로 설계·구현한 공개용 프로젝트**입니다.

> 이 저장소는 특정 회사의 원본 소스코드를 복제한 프로젝트가 아닙니다. 실제 회사명, 고객 데이터, 내부 테이블명, 사내 URL, 계정, API Key, 업무 기밀 정보는 포함하지 않습니다. 업무 도메인에서 얻은 일반적인 설계 경험을 바탕으로 별도 구현했습니다.

## 핵심 기능

- JWT 기반 관리자/센터 사용자 인증
- 고객 서비스 접수 Master-Detail 등록 및 조회
- 제품별 증상/수리 상태 관리
- 접수 상태 Dashboard 집계
- 외부 알림/배송/ERP 연동을 Adapter 패턴의 Mock Client로 분리
- 5년 경과 개인정보 자동 마스킹 Scheduler
- React SPA + Spring Boot REST API 분리 구조
- H2 Demo profile / PostgreSQL 운영형 profile 분리

## Architecture

```text
React + Vite
     |
     | REST / JSON + Bearer JWT
     v
Spring Boot
  ├─ Controller
  ├─ Service
  ├─ Repository (JPA)
  ├─ Security (JWT)
  ├─ Integration Adapter
  └─ Scheduler
     |
     +---- H2 (demo)
     |
     +---- PostgreSQL (postgres profile)

External systems
  ├─ NotificationClient (Mock)
  ├─ DeliveryClient (Mock)
  └─ ErpClient (Mock)
```

자세한 내용은 [`docs/architecture.md`](docs/architecture.md), [`docs/erd.md`](docs/erd.md), [`docs/api.md`](docs/api.md), [`docs/reviewer-guide.md`](docs/reviewer-guide.md)를 참고하세요.

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.4.12
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT (JJWT)
- Bean Validation
- PostgreSQL / H2
- Gradle

### Frontend
- React 19.2.8
- Vite 8.2.2
- React Router 7.18.3
- Axios 1.20.0
- Vanilla CSS

## 실행 방법

### 1. Demo 모드 — DB 설치 없이 실행

```bash
cd backend
./gradlew bootRun
```

Windows:

```powershell
cd backend
.\gradlew.bat bootRun
```

Backend: `http://localhost:8080`

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend: `http://localhost:5173`

Demo 계정:

```text
ADMIN  : admin / admin1234
CENTER : center / center1234
```

### 3. PostgreSQL profile

```bash
cp .env.example .env
docker compose up -d
cd backend
SPRING_PROFILES_ACTIVE=postgres ./gradlew bootRun
```

Windows PowerShell:

```powershell
Copy-Item .env.example .env
docker compose up -d
cd backend
$env:SPRING_PROFILES_ACTIVE="postgres"
.\gradlew.bat bootRun
```

## 주요 API

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | 로그인 및 JWT 발급 |
| GET | `/api/auth/me` | 현재 로그인 사용자/권한 조회 |
| GET | `/api/dashboard` | 상태별 접수 현황 |
| GET | `/api/requests` | 접수 목록 |
| POST | `/api/requests` | 신규 접수 |
| GET | `/api/requests/{id}` | 접수 상세 |
| PATCH | `/api/requests/{id}/status` | 접수 상태 변경 |
| POST | `/api/integrations/{id}/notification` | 고객 알림 Mock 전송 |
| POST | `/api/integrations/{id}/erp` | ERP Mock 전송 (ADMIN only) |
| GET | `/api/integrations/{id}/delivery` | 배송상태 Mock 조회 |

## 설계 포인트

### 1. 회사 고유 시스템과 공개 포트폴리오의 분리
실무 프로젝트의 고유 테이블 및 내부 API를 재사용하지 않고 `ServiceRequest`, `ServiceItem`이라는 일반화된 도메인 모델로 새롭게 설계했습니다.

### 2. 외부 연동 추상화
실제 ERP, 알림 서비스, 택배 API 대신 Interface + Mock Adapter를 사용했습니다. 공개 저장소에는 외부 서비스 Secret이 존재하지 않으며 나중에 실제 API Adapter를 추가해도 Service Layer 변경을 최소화할 수 있습니다.

### 3. 보안
Spring Security Filter Chain에서 인증/인가를 처리하고 Controller가 Session을 직접 검사하지 않도록 구성했습니다. JWT Secret은 환경변수로 주입할 수 있으며, ERP 전송 기능은 `@PreAuthorize`로 ADMIN 권한에 제한했습니다.

### 4. 개인정보 Life Cycle
접수일 기준 5년이 지난 고객의 전화번호, 이메일, 주소를 마스킹하는 Scheduler를 구현했습니다. 실제 운영에서는 보존기간을 법무/보안 정책에 맞게 설정해야 합니다.

## 5분 리뷰 시나리오

1. `admin / admin1234`로 로그인해 JWT 인증을 확인합니다.
2. Service Requests에서 하나의 접수에 제품을 2개 이상 추가해 Master-Detail 저장을 확인합니다.
3. 상세 화면에서 접수 상태를 변경합니다.
4. Notification / Delivery Mock Adapter를 호출합니다.
5. ADMIN 계정에서만 ERP Mock Transfer가 표시·허용되는지 확인합니다.
6. `http://localhost:8080/actuator/health`에서 Backend health endpoint를 확인합니다.

## Repository 공개 전 체크리스트

- `.env`가 commit되지 않았는지 확인
- Git history에 Secret이 포함되어 있지 않은지 확인
- 실제 고객/회사/사원 데이터가 없는지 확인
- 원본 회사 프로젝트의 이미지, 로고, 문서가 없는지 확인
- README에 포트폴리오 재구현 프로젝트임을 명시

## Portfolio Description

> 서비스센터의 고객 접수부터 수리 상태 관리, 외부 알림·배송·ERP 연동까지의 업무 흐름을 모델링한 웹 플랫폼입니다. 기존 업무 시스템 개발 경험을 바탕으로 공개 가능한 도메인으로 재설계했으며, Spring Boot REST API와 React SPA를 분리하고 JWT 인증, JPA Master-Detail 모델링, Integration Adapter, 개인정보 마스킹 Scheduler를 적용했습니다.
