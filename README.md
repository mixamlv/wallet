# Getting Started

Docker and Java 21 is required 

### Run spring boot app

    gradlew bootRun

Swagger UI is available at:

http://localhost:8080/v3/webjars/swagger-ui/index.html

### Unit tests

    gradlew test

### Integration tests

Integration tests depends on api-client module.
To generate api client classes following tasks needs to be executed:

Generate OpenAPI yaml (build\openapi.yaml):

    gradlew generateOpenApiDocs

Generate API Client classes (api-client\build\generated\java\src\main):

    gradlew openApiGenerate

Now integration tests can be executed:

    gradlew intTest

### Development

While developing by default db (postgresql) and exchangerates provider (mock server) are setup automatically using docker, see [compose.yaml](compose.yaml) for more details

### Exchangerates configuration

In order to configure exchange rate service (https://exchangerate.host/documentation), provide following configs props in application.yaml:

- infra.exchangerate.baseUrl
- infra.exchangerate.apiKey
- infra.exchangerate.bseCurrency