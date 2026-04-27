# Song Record Collection API

A Spring Boot 3.x API demonstrating three API paradigms on the same dataset.

## Tech Stack
- Java 21 | Spring Boot 3.x | Maven
- Spring Security (Basic Auth)
- Swagger UI (springdoc-openapi 2.8.5)
- GraphQL (spring-graphql)
- Kong API Gateway

## API Paradigms

### REST
Resource-oriented endpoints
- GET  /v1/api/rest/artists
- GET  /v1/api/rest/artist/{artistName}
- POST /v1/api/rest/artist

### RPC
Action-oriented endpoints
- POST /v1/api/rpc/listArtists
- POST /v1/api/rpc/getArtist
- POST /v1/api/rpc/createArtist

### GraphQL
Single endpoint at /graphql
- query { artists(page: 0, size: 10) { id artist genre albums } }
- query { artist(artistName: "SZA") { artist genre } }
- mutation { createArtist(input: { artist: "Drake", genre: "Hip-Hop", albums: 7 }) { id } }

## Running the App

### Spring Boot
mvn spring-boot:run

### URLs
- Swagger UI:  http://localhost:8080/swagger-ui/index.html
- GraphiQL:    http://localhost:8080/graphiql.html
- API Docs:    http://localhost:8080/v3/api-docs

### Credentials
- Username: admin
- Password: secret123

## Kong API Gateway

cd kong
docker-compose up -d

### Proxy URL
http://localhost:8000/v1/api/rest/artists

### Plugins
- Rate Limiting: 5 requests/minute → 429
- Request Size Limiting: 1KB max → 413
