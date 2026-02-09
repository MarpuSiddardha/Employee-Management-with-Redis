# Redis Employee Management

A Spring Boot application demonstrating Redis integration with caching and rate limiting for employee management operations.

## Technologies Used

- **Spring Boot 3.5.10** - Main framework
- **Java 17** - Programming language
- **Spring Data Redis** - Redis integration
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database (for development)
- **PostgreSQL** - Production database support
- **Lombok** - Reduces boilerplate code
- **Spring Validation** - Request validation
- **Jackson** - JSON serialization

## Project Structure

### Model
- `Employee` - Entity class with id, name, role, and salary fields

### Controllers
- `EmployeeController` - REST endpoints for employee operations:
  - GET `/api/employees/{id}` - Get employee by ID
  - GET `/api/employees/userId/{id}` - Get employee with rate limiting
  - GET `/api/employees` - Get all employees
  - POST `/api/employees` - Create new employee
  - PATCH `/api/employees/{id}` - Partial update employee
  - PUT `/api/employees/{id}` - Replace employee
  - DELETE `/api/employees/{id}` - Delete employee

### Services
- `EmployeeService` - Business logic with Redis caching:
  - Uses `@Cacheable` for read operations
  - Uses `@CachePut` and `@CacheEvict` for update/delete operations
- `RedisLimiterService` - Rate limiting service:
  - Limits 5 requests per minute per user
  - Returns remaining TTL when limit exceeded

### Configuration
- `RedisConfig` - Redis and cache configuration:
  - JSON serialization for cache values
  - 5-minute TTL for cache entries
  - Null value caching disabled

### DTOs
- `EmployeeRequest` - Request DTO for create/update operations
- `EmployeePartialUpdateRequest` - Request DTO for partial updates
- `EmployeeResponse` - Response DTO

### Exception Handling
- `EmployeeNotFoundException` - Custom exception for missing employees
- `GlobalExceptionHandler` - Centralized exception handling

### Utilities
- `EmployeeMapper` - Maps between entities and DTOs

## Features

### Caching Strategy
- **Read-through**: Cache miss triggers database query and caches result
- **Write-through**: Updates immediately update cache
- **Cache eviction**: Updates clear relevant cache entries
- Cache names: `employee` (single), `employees` (all)

### Rate Limiting
- 5 requests per minute per user ID
- Uses Redis counters with TTL
- Returns HTTP 429 with retry-after time when exceeded

### Database
- H2 console available at `/h2-console` (development)
- PostgreSQL support configured for production

## Running the Application

1. Start Redis server on localhost:6379
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. Access H2 console: http://localhost:8080/h2-console
4. API base URL: http://localhost:8080/api/employees

## Configuration

- Redis: localhost:6379, database 0
- H2: mem:testdb, username: sa, password: root
- Cache TTL: 5 minutes
- Rate limit: 5 requests/minute per user
