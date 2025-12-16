# Multi-stage build를 사용하여 최적화된 이미지 생성
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Gradle 캐시를 활용하기 위해 build.gradle과 settings.gradle을 먼저 복사
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

# 의존성 다운로드 (레이어 캐싱 최적화)
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN gradle clean build -x test --no-daemon

# 실행 단계
FROM eclipse-temurin:21-jre-alpine

# 한국 시간대 설정
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone && \
    apk del tzdata

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 18080

# 헬스체크
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:18080/api/actuator/health || exit 1

# 애플리케이션 실행 (한국 시간대 환경변수 설정)
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]

