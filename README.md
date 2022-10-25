# FX Price Handler App

## Information

Hello, this is a simple application allowing for handling of FX price feed

The app connects to PostgreSQL database.

CI/CD using Circle CI

Whole infrastructure utilizes ECS cluster with custom EC2 instances.

## Tech
* Java 11
* Spring Boot 2
* CircleCI
* Liquibase
* PostgreSQL
* Docker
* REST
* Lombok
* MapStruct
* Swagger

## Endpoints

Circle CI: `https://app.circleci.com/pipelines/github/patrykkrawczyk/fx-price-handler`

API: `http://localhost:8080/api`

Swagger: `http://localhost:8080/v2/api-docs`

Swagger UI: `http://localhost:8080/swagger-ui/`

Health: `http://localhost:8080/actuator/health`

## Running locally

```
./mvnw clean package
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
docker-compose -f docker/app.yml up -d --build

docker-compose -f docker/app.yml down
```

## Development

Start DB: `docker-compose -f docker/postgresql.yml up -d`

Stop DB: `docker-compose -f docker/postgresql.yml down`

Run app: `./mvnw`

## Testing

`./mvnw verify`

