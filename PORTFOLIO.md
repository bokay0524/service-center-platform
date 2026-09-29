# Portfolio Project — Service Center Platform

## 프로젝트 한 줄 소개
고객 서비스 접수부터 제품별 수리 관리, 외부 시스템 연동, 역할 기반 접근 제어, 개인정보 보존정책까지 구현한 서비스센터 업무 플랫폼입니다.

## 개발 목적
실무에서 경험한 서비스센터 업무 흐름을 공개 가능한 범위에서 일반화하고, 회사 자산을 사용하지 않은 개인 포트폴리오 프로젝트로 새롭게 설계·구현했습니다.

## 기술 스택
- Backend: Java 17, Spring Boot 3.4.12, Spring Security, Spring Data JPA, JJWT
- Frontend: React 19.2.8, Vite 8.2.2, React Router, Axios
- Database: H2 Demo / PostgreSQL
- Infra: Docker Compose, GitHub Actions, Spring Boot Actuator

## 구현 범위
- Spring Boot REST API 및 Domain Model 설계
- Spring Security + JWT 인증/인가
- 로그인 사용자 조회(`/api/auth/me`)
- JPA Master-Detail 관계 설계
- 하나의 접수에 다수 제품을 등록하는 React UI
- 접수 상태 Dashboard 및 Workflow 관리
- ERP / Notification / Delivery Adapter 구조 설계
- ADMIN 전용 ERP 전송 권한 제어
- 개인정보 자동 마스킹 Scheduler
- PostgreSQL 로컬 환경 Docker Compose 구성
- Backend Test / Frontend Build GitHub Actions 구성

## Architecture

```text
React SPA
   │ Bearer JWT
   ▼
Spring Security
   ▼
REST Controller
   ▼
Application Service
   ├───────────────┐
   ▼               ▼
JPA Repository   Integration Port
   │               ├─ NotificationClient
   ▼               ├─ DeliveryClient
H2/PostgreSQL      └─ ErpClient
                   │
                   ▼
                 Mock Adapter
```

## 면접에서 강조할 기술적 선택

### 1. 인증 로직과 Controller의 분리
Controller에서 Session 값을 직접 확인하는 방식 대신 Spring Security Filter Chain과 JWT 인증을 사용해 인증과 비즈니스 로직의 관심사를 분리했습니다.

### 2. UI 숨김과 서버 권한 검사의 분리
ERP 전송 버튼은 CENTER 화면에서 보이지 않게 처리하는 동시에 Backend에서도 `@PreAuthorize("hasRole('ADMIN')")`로 검증합니다. UI 제어만으로 권한을 구현하지 않았습니다.

### 3. Master-Detail Aggregate
한 고객 접수에 여러 제품이 등록될 수 있는 업무 특성을 `ServiceRequest`와 `ServiceItem`의 1:N 관계로 표현하고, React 등록 화면에서도 Detail Row를 동적으로 추가할 수 있게 구현했습니다.

### 4. 외부 API를 Interface로 추상화
외부 ERP, 메시징, 배송 서비스를 Application 코드에 직접 결합하지 않고 Port Interface로 분리했습니다. 공개 프로젝트에서는 실제 회사 Endpoint나 Secret 없이 Mock Adapter만 제공합니다.

### 5. Demo / PostgreSQL Profile 분리
리뷰어는 H2를 사용해 별도 DB 설치 없이 실행할 수 있고, 운영형 구조를 확인하려는 경우 PostgreSQL profile과 Docker Compose를 사용할 수 있습니다.

### 6. 개인정보 Life Cycle
접수 데이터 저장뿐 아니라 보존기간 경과 후 고객 전화번호, 이메일, 주소를 비식별화하는 Scheduler를 구현해 운영 단계의 개인정보 Lifecycle도 고려했습니다.

## 테스트
- 접수 생성 시 Master-Detail Aggregate 생성 검증
- 상태 변경 시 Master와 Detail 상태 동기화 검증
- Dashboard 집계 검증
- JWT 발급/검증 및 비정상 Token 거부 검증
- GitHub Actions에서 Backend Test와 Frontend Build 수행

## 원본 회사 프로젝트와의 관계
이 프로젝트는 특정 회사의 원본 Repository를 복제하거나 이름만 변경한 소스가 아닙니다. 실제 회사명, 로고, 내부 DB 테이블명, 서버 주소, 사용자 데이터, API Secret, 외부 서비스 Credential을 사용하지 않고 실무에서 학습한 일반적인 업무 흐름과 설계 경험을 바탕으로 별도 구현했습니다.

## 다음 개선 후보
- Flyway 기반 DB Migration
- Testcontainers 기반 PostgreSQL Repository 통합 테스트
- Redis 기반 OTP / Rate Limit
- OpenAPI / Swagger 문서화
- S3 호환 Object Storage 이미지 업로드 Adapter
- Refresh Token 및 Token Rotation
