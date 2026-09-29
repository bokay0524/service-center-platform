# REST API

모든 API 응답은 JSON이며 `/api/auth/login`을 제외한 업무 API는 Bearer JWT가 필요합니다.

## Auth

### POST `/api/auth/login`

```json
{
  "username": "admin",
  "password": "admin1234"
}
```

Response:

```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer",
  "role": "ADMIN"
}
```

### GET `/api/auth/me`

Header:

```text
Authorization: Bearer <jwt>
```

Response:

```json
{
  "username": "admin",
  "role": "ADMIN"
}
```

## Service Requests

### GET `/api/requests`

접수 목록과 각 접수의 제품 목록을 반환합니다.

### POST `/api/requests`

하나의 접수 Master에 여러 Service Item을 동시에 등록할 수 있습니다.

```json
{
  "customerName": "Demo Customer",
  "customerPhone": "010-1234-5678",
  "customerEmail": "demo@example.com",
  "customerAddress": "Seoul",
  "branchCode": "SEOUL-01",
  "items": [
    {
      "productCategory": "Audio",
      "modelName": "MODEL-A",
      "serialNo": "SN-DEMO-001",
      "symptom": "Power does not turn on"
    },
    {
      "productCategory": "Accessory",
      "modelName": "MODEL-B",
      "serialNo": "SN-DEMO-002",
      "symptom": "Intermittent connection"
    }
  ]
}
```

### GET `/api/requests/{id}`

접수 Master와 제품 Detail을 조회합니다.

### PATCH `/api/requests/{id}/status`

```json
{
  "status": "REPAIRING"
}
```

Available statuses:

`RECEIVED`, `INSPECTING`, `REPAIRING`, `WAITING_PARTS`, `COMPLETED`, `SHIPPED`, `CANCELLED`

## Integration Adapter

| Method | Endpoint | 권한 | 설명 |
|---|---|---|---|
| POST | `/api/integrations/{id}/notification` | Authenticated | 고객 알림 Mock Adapter |
| GET | `/api/integrations/{id}/delivery` | Authenticated | 배송조회 Mock Adapter |
| POST | `/api/integrations/{id}/erp` | ADMIN | ERP 전송 Mock Adapter |

실제 회사/외부 서비스 Endpoint나 Secret은 포함하지 않으며 Interface 기반 Mock 구현만 제공합니다.

## Health

### GET `/actuator/health`

Spring Boot Actuator의 최소 health endpoint입니다. 인증 없이 상태 확인이 가능합니다.
