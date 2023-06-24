# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy
RUN addgroup --system paymentchain && adduser --system --ingroup paymentchain admin
USER admin:paymentchain
VOLUME /tmp
ARG JAR_FILE=target/*.jar
ADD target/${JAR_FILE} app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.edg=file:/dev/./urandom -jar /app.jar"]