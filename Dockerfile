# 첫 번째 스테이지 : 빌드 환경 설정
FROM bellsoft/liberica-openjdk-alpine:17 AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# 두 번째 스테이지 : 실행 환경 설정
FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 9008
ENTRYPOINT ["java", "-jar", "app.jar"]
