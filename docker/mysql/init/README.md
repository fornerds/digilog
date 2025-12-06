# MySQL 초기화 스크립트

이 디렉토리의 SQL 파일들은 MySQL 컨테이너가 처음 시작될 때 자동으로 실행됩니다.

## 파일 순서
1. `01-init-schema.sql` - 스키마 초기화 (필요시)
2. `02-insert-dummy-data.sql` - 더미 데이터 삽입

## 주의사항
- 파일은 알파벳 순서로 실행됩니다
- 컨테이너가 이미 생성된 경우, 볼륨에 데이터가 있으면 실행되지 않습니다
- 더미 데이터를 다시 삽입하려면 볼륨을 삭제하고 컨테이너를 재생성해야 합니다:
  ```bash
  docker-compose --env-file .env.dev down -v
  docker-compose --env-file .env.dev up -d --build
  ```

## JPA 설정
- JPA가 데이터베이스를 건드리지 않도록 `.env.dev`에서 `JPA_DDL_AUTO=validate`로 설정되어야 합니다
- `update`나 `create`로 설정하면 JPA가 스키마를 변경할 수 있습니다

## 더미 사용자 계정
- 이메일: `user1@example.com` ~ `user6@example.com`
- 비밀번호: `password123`
- 관리자 계정: `admin@example.com` / `password123`
