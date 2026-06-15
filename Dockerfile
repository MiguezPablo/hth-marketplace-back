FROM gradle:8.4.0-jdk21 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle

RUN gradle dependencies --no-daemon || true

COPY src src

RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

ENV TZ=America/Argentina/Buenos_Aires

RUN apk add --no-cache tzdata curl && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo "$TZ" > /etc/timezone && \
    addgroup -S appuser && \
    adduser -S appuser -G appuser

WORKDIR /app

COPY --from=build /app/build/libs/hth-marketplace.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
