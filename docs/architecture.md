# Architecture

## 목표

포트폴리오 프로젝트의 핵심 목표는 **실무 도메인 경험은 보여주되 회사 고유 구현은 노출하지 않는 것**입니다.

## Backend Layer

```text
HTTP Request
   ↓
Security Filter (JWT)
   ↓
Controller
   ↓
Application Service
   ↓
Repository / Integration Port
   ↓
JPA DB / Mock External Adapter
```

### Controller
HTTP request/response와 Bean Validation을 담당합니다.

### Service
접수번호 생성, Master-Detail 접수 등록, 상태 변경 등 비즈니스 흐름을 담당합니다.

### Repository
Spring Data JPA를 사용하여 Domain Entity를 저장합니다.

### Integration Port
ERP, 고객 알림, 배송조회와 같은 외부 시스템을 Interface로 분리했습니다. 현재 공개 프로젝트에서는 Mock 구현체를 사용합니다.

```text
Application
   ├─ NotificationClient ─ MockNotificationClient
   ├─ DeliveryClient     ─ MockDeliveryClient
   └─ ErpClient          ─ MockErpClient
```

외부 시스템을 교체할 때 Application/Controller에 구체 API 구현을 직접 결합하지 않는 것이 목적입니다.

### Scheduler
보존기간이 지난 고객 개인정보를 자동 비식별화합니다.

## Security

```text
POST /api/auth/login
    ↓
Username / Password 검증
    ↓
JWT 발급
    ↓
Authorization: Bearer <token>
    ↓
JwtAuthenticationFilter
    ↓
SecurityContext
    ↓
Method Authorization (@PreAuthorize)
```

공개 Endpoint는 로그인, H2 Console(Demo), Actuator health로 최소화했습니다. `/api/auth/me`와 모든 업무 API는 인증이 필요하며 ERP Mock 전송은 `ADMIN` 역할에만 허용합니다.

## Frontend

React SPA에서는 Axios interceptor가 JWT를 Authorization Header에 첨부합니다. 401 응답 시 Demo 인증정보를 제거하고 Login 화면으로 이동합니다.

접수 화면은 하나의 고객 접수에 여러 제품을 동적으로 추가할 수 있어 Backend의 Master-Detail Aggregate가 UI에서도 드러나도록 구성했습니다.

## Profiles

- `demo`: H2 In-Memory DB, 별도 DB 설치 없이 리뷰 가능
- `postgres`: PostgreSQL 연결, Docker Compose로 로컬 실행 가능

환경값은 `.env.example`과 OS 환경변수 예시만 제공하며 실제 Secret은 저장소에 포함하지 않습니다.
