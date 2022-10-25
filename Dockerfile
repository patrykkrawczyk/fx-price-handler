FROM openjdk:11-jdk-slim

RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd -r spring && useradd --no-log-init -r -g spring spring
USER spring:spring
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
RUN cat /app/pro/patrykkrawczyk/fxpricehandler/FxPriceHandlerApp.class

ENTRYPOINT ["java","-cp","app:app/lib/*","pro.patrykkrawczyk.fxpricehandler.FxPriceHandlerApp"]
