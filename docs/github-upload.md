# GitHub Upload Guide

## 1. 공개 전 마지막 확인

```bash
git grep -n -i "password\|secret\|api_key\|api-key"
git status
```

`.env`, 실제 개인정보, 회사 내부 URL/테이블명/로고가 없는지 다시 확인합니다.

## 2. 새 Git Repository 생성

프로젝트 루트에서:

```bash
git init
git branch -M main
git add .
git commit -m "feat: initialize service center portfolio project"
```

GitHub에서 빈 repository를 만든 뒤:

```bash
git remote add origin https://github.com/<YOUR_ID>/service-center-platform.git
git push -u origin main
```

## 3. 추천 Repository 설명

```text
Service center workflow portfolio — Spring Boot, React, JPA, JWT, PostgreSQL and mock external integrations.
```

## 4. GitHub About 추천 Topics

```text
spring-boot react java jpa postgresql jwt portfolio rest-api
```
