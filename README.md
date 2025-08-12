### POC: OAuth2 + Spring Boot (Users API, H2, Circuit Breaker, Contracts, Docker)

This POC demonstrates a secure REST microservice with OAuth2 JWT auth, scope-based authorization, H2-backed users, circuit breaker with fallback, API contracts, and containerization.

- Framework: Spring Boot 3 (Java 17)
- Security: OAuth2 Resource Server (JWT RS256), dev token issuance
- Persistence: H2 (in-memory) + Spring Data JPA
- Resilience: Resilience4j Circuit Breaker + WebClient
- Docs: OpenAPI/Swagger UI
- Observability: Actuator (health/info)
- Testing: JUnit/MockMvc, WireMock, Spring Cloud Contract
- Packaging: Docker multi-stage image

## High-Level Design (HLD)

- Client calls public endpoints or obtains a JWT (dev) via POST /api/token
- JWT-protected endpoints are enforced by Spring Security (scopes read, write)
- UserController performs create/list via UserService backed by H2 through JPA (UserRepository)
- ExternalServiceClient makes outbound HTTP calls using WebClient, guarded by a Resilience4j circuit breaker with a graceful fallback
- API is documented with OpenAPI and exposes health via Actuator

## Low-Level Design (LLD)

- Package layout
  - config: SecurityConfig, WebClientConfig, OpenApiConfig
  - controller: PublicController, UserController, TokenController, ResilienceController
  - entity: UserEntity
  - model: CreateUserRequest, User
  - repository: UserRepository
  - service: UserService, ExternalServiceClient
  - exception: GlobalExceptionHandler
- Security
  - Resource server with JWT (RS256). Keys generated in-memory at startup (dev only)
  - Scopes: read for GET /api/users, write for POST /api/users
  - Dev token endpoint: POST /api/token using basic username/password auth (user/password, admin/password)
- Persistence
  - H2 in-memory DB, schema auto-managed (ddl-auto: update)
- Resilience
  - ExternalServiceClient.fetchStatus() uses WebClient + @CircuitBreaker(name = "externalService")
  - Fallback returns { "status": "fallback", "message": "external service unavailable" }
- Testing
  - MockMvc ITs for public and secured flows, JWT created via encoder
  - WireMock IT for circuit breaker fallback path
  - Spring Cloud Contract: producer contract for GET /api/public/success

## Configuration

Main settings in src/main/resources/application.yml:
- Server: server.port: 8080
- DataSource (H2):
  - spring.datasource.url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
  - spring.jpa.hibernate.ddl-auto: update
  - H2 console enabled at /h2-console
- Resilience4j: circuit breaker instance externalService tuned with sensible defaults
- Actuator: exposes health,info
- Swagger UI: /swagger-ui/index.html
- External service base URL (for demo): external.service.base-url (defaults to https://httpbin.org)

## Build and Run

Prerequisites: Java 17, Maven, Docker (optional)

- Build
```bash
mvn clean package
```
- Run (local)
```bash
mvn spring-boot:run
```
- **Run with Keycloak profile** (uses issuer-uri):
```bash
docker compose up -d keycloak
mvn spring-boot:run -Dspring-boot.run.profiles=keycloak
```
- Run tests (unit, integration, WireMock, contracts)
```bash
mvn clean test
```

## Obtain a JWT (dev-only)

- Default users: user/password, admin/password
- Request a token (include desired scopes):
```bash
curl -X POST http://localhost:8080/api/token \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password","scopes":["read","write"]}'
```
- Use the access_token in API calls: Authorization: Bearer <token>

## Using Keycloak

- Start Keycloak: `docker compose up -d keycloak`
- Console: `http://localhost:8081/` (admin/admin)
- Realm: `demo-realm`
- Client: `springboot-api` (public)
- Test user: `api-user/password`
- Get token via Direct Access Grant:
```bash
curl -X POST "http://localhost:8081/realms/demo-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=springboot-api&username=api-user&password=password"
```
- Use the `access_token` from response with the API.

## API Endpoints

- Public (no auth)
  - GET /api/public/success
  - GET /api/public/fallback
  - GET /api/resilience/status (calls external service, returns fallback on failure)
- Users (JWT required)
  - POST /api/users (scope write)
    - Body example: { "name": "Jane Doe", "email": "jane@example.com" }
  - GET /api/users (scope read)

## Swagger and Actuator

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
- Health: http://localhost:8080/actuator/health
- H2 console: http://localhost:8080/h2-console (JDBC URL jdbc:h2:mem:testdb, user sa)

## Docker

- Build image
```bash
docker build -t poc-oauth-springboot:latest .
```
- Run container
```bash
docker run --rm -p 8080:8080 poc-oauth-springboot:latest
```
- Customize JVM
```bash
docker run --rm -e JAVA_OPTS="-Xms256m -Xmx512m" -p 8080:8080 poc-oauth-springboot:latest
```

## Notes and Next Steps

- Keys are generated in-memory on each start; for production, use an external IdP (e.g., Keycloak, Cognito) or a managed JWKS
- The /api/token endpoint is for POC/dev only. In production, remove it and integrate with your OAuth provider
- Replace H2 with a production RDBMS and use migrations (Flyway/Liquibase)
- Expand contracts for secured endpoints (with stubbed auth) and add performance/security testing
