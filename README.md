# Blog API

Required Java version 23.0.2

## Development server

To start a local development server, run:

```bash
mvnw spring-boot:run
```

Swagger UI docs available at: http://localhost:8080/swagger-ui/index.html#/

H2 in-memory database overview available at: http://localhost:8080/h2-console with credentials:
username: sa,
password: \<empty> , jdbc url set to: jdbc:h2:mem:testdb , driver class: org.h2.Driver
