-- MySQL 문자셋 및 시간대 설정
-- 이 파일은 가장 먼저 실행되어야 하므로 00- 접두사 사용

-- 서버 레벨 문자셋 설정
SET character_set_server = 'utf8mb4';
SET collation_server = 'utf8mb4_unicode_ci';

-- 서버 레벨 시간대 설정 (한국 시간: UTC+9)
SET GLOBAL time_zone = '+09:00';
SET time_zone = '+09:00';

-- 데이터베이스 문자셋 설정
ALTER DATABASE digilog_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE digilog_dev;

-- 세션 문자셋 설정
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_client = 'utf8mb4';
SET character_set_connection = 'utf8mb4';
SET character_set_results = 'utf8mb4';

-- 세션 시간대 설정 (한국 시간: UTC+9)
SET time_zone = '+09:00';

