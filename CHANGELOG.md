# Changelog

## 0.2.0

- Added authenticated `/api/auth/me` endpoint.
- Restricted mock ERP transfer to ADMIN with method-level authorization.
- Added multi-item service request form to expose the Master-Detail model in the UI.
- Added Spring Boot Actuator health endpoint.
- Added JWT, dashboard, and status update tests.
- Pinned frontend dependency versions for reproducible review.
- Added reviewer guide and expanded API/architecture documentation.
- Moved demo JWT secret to the demo profile; PostgreSQL profile requires `JWT_SECRET`.

## 0.1.0

- Initial public portfolio implementation.
