# Reviewer Guide

이 저장소를 짧은 시간 안에 검토할 때 추천하는 순서입니다.

## 1. 인증

`admin / admin1234`로 로그인합니다. Spring Security가 사용자 인증을 처리하고 JWT를 발급합니다. 이후 React Axios interceptor가 Bearer Token을 각 API 요청에 첨부합니다.

## 2. Master-Detail 등록

Service Requests → New Request에서 제품을 2개 이상 추가해 접수합니다.

Backend에서는 `ServiceRequest`가 Master, `ServiceItem`이 Detail이며 `@OneToMany` / `@ManyToOne` 관계와 Cascade로 하나의 Aggregate를 구성합니다.

## 3. Workflow

생성된 접수의 상세 화면에서 상태를 변경합니다. Master와 포함된 Service Item의 상태가 Transaction 안에서 함께 변경됩니다.

## 4. 외부 연동 추상화

Notification, Delivery, ERP 버튼은 실제 외부 시스템이 아니라 Port Interface + Mock Adapter 구조를 호출합니다.

특히 ERP 전송은 Backend `@PreAuthorize("hasRole('ADMIN')")`로 제한되어 있어 단순 화면 숨김이 아니라 서버에서도 권한을 검사합니다.

## 5. 개인정보 Lifecycle

`PrivacyMaskingScheduler`는 설정된 보존기간보다 오래된 접수의 전화번호, 이메일, 주소를 마스킹합니다. 실제 보존기간은 회사 정책/법적 요구사항에 맞춰 설정해야 한다는 전제를 문서에 명시합니다.

## 6. 운영 관점

`/actuator/health`에서 Backend 상태 확인이 가능하며 DB/JWT/CORS 값은 환경변수로 분리할 수 있습니다.
