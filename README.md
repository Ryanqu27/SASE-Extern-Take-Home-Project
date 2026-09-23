# URL Shortener

A simple backend service built with Java Spring Boot. Submit a long URL, receive a short code, and get redirected when you visit it.

## Prerequisites
- Java 21 or higher

## How to Run
Navigate to the url-shortener folder and run: 
```bash
./mvnw spring-boot:run
```
On Windows, use `.\mvnw.cmd spring-boot:run` instead.

## API Demonstration 
Swagger: http://localhost:8080/swagger-ui.html

## Design Decisions

### Short Code Generation
Codes are 8-character alphanumeric strings generated using `SecureRandom`. With 62 possible characters and 8 positions, there are 62⁸ possible codes, making collisions extremely unlikely. The service retries up to 5 times if a collision occurs.

### Click Tracking
The redirect endpoint (`GET /{code}`) atomically increments a click counter via a JPQL `UPDATE` query. This prevents race conditions from two requests hitting simultaneously.

### Layered Architecture
The code follows a standard Controller to Service to Repository pattern:
- **Controller** handles HTTP requests
- **Service** contains business logic
- **Repository** handles database access 

### Input Validation
The ShortenRequestDto uses Bean Validation annotations of @NotBlank and @URL to reject invalid input at the controller layer before it reaches the service.

### HTTP 302 Redirect
A 301 redirect will cache the result in my browser which means that subsequent clicks never hit the backend and increment the click counter. A 302 will ensure the browser hits the backend on every visit, correctly incrementing the counter. 