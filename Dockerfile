FROM eclipse-temurin:17-jre

RUN addgroup appuser && adduser --disabled-password appuser --ingroup appuser

RUN mkdir /app

COPY --chown=appuser:appuser build/libs/application.jar /app/application.jar
WORKDIR /app

USER appuser
EXPOSE 8080
CMD ["java", "-jar", "application.jar"]