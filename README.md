# Redis Employee Management System

A comprehensive Spring Boot application for employee management with Redis caching, JWT authentication, rate limiting, and monitoring capabilities.

## 🚀 Features

### Core Functionality
- **Employee Management**: Full CRUD operations for employee records
- **User Authentication**: JWT-based authentication with role-based access control
- **Redis Caching**: Multi-level caching strategy for performance optimization
- **Rate Limiting**: Redis-based rate limiting to prevent API abuse
- **Monitoring**: Actuator endpoints with Prometheus metrics
- **AOP Logging**: Method execution time tracking

### Technical Features
- **Spring Boot 3.5.10** with Java 17
- **Spring Security** with JWT authentication
- **Spring Data JPA** with H2 (development) and PostgreSQL (production)
- **Redis** for caching and rate limiting
- **Lombok** for boilerplate code reduction
- **Validation** with Jakarta Bean Validation
- **AOP** for cross-cutting concerns

## 📁 Project Structure

```
src/main/java/com/siddardha/redisDemonstration/
├── AOP/
│   └── ExecutionTimeAspect.java          # Method execution time logging
├── Config/
│   ├── AdminConfig.java                  # Admin user configuration
│   ├── AdminInitializer.java             # Admin user initialization
│   ├── RedisConfig.java                  # Redis configuration
│   └── SecurityConfig.java               # Spring Security configuration
├── Controller/
│   ├── AuthController.java               # User authentication endpoints
│   └── EmployeeController.java           # Employee CRUD endpoints
├── DTO/
│   ├── EmployeePartialUpdateRequest.java # Partial update DTO
│   ├── EmployeeRequest.java              # Employee creation/update DTO
│   ├── EmployeeResponse.java             # Employee response DTO
│   ├── UserRequest.java                  # User request DTO
│   └── UserResponse.java                 # User response DTO
├── Exception/
│   ├── EmployeeAlreadyExistsException.java
│   ├── EmployeeNotFoundException.java
│   ├── GlobalExceptionHandler.java      # Centralized exception handling
│   ├── UserAlreadyExistsException.java
│   └── UserNotFoundException.java
├── Mapper/
│   ├── EmployeeMapper.java               # Entity-DTO mapping
│   └── UserMapper.java                   # User entity-DTO mapping
├── Model/
│   ├── Employee.java                     # Employee entity
│   ├── Role.java                         # Role entity
│   └── User.java                         # User entity
├── Repository/
│   ├── EmployeeRepository.java           # Employee data access
│   ├── RoleRepository.java               # Role data access
│   └── UserRepository.java               # User data access
├── Service/
│   ├── CustomUserDetailsService.java     # Custom user details service
│   ├── EmployeeService.java              # Employee business logic
│   ├── RedisLimiterService.java          # Rate limiting service
│   └── UserService.java                  # User business logic
├── Util/
│   ├── JwtAuthenticationFilter.java     # JWT authentication filter
│   └── JwtUtil.java                      # JWT token utilities
└── RedisDemonstrationApplication.java    # Main application class

Root Configuration Files:
├── .gitignore                            # Git ignore patterns
├── HELP.md                               # Spring Boot help documentation
├── mvnw, mvnw.cmd                       # Maven wrapper scripts
├── pom.xml                               # Maven project configuration
└── README.md                             # Project documentation
```

## 🛠️ Dependencies

### Core Spring Boot Starters
- `spring-boot-starter-web` - REST API framework
- `spring-boot-starter-data-jpa` - Database operations
- `spring-boot-starter-data-redis` - Redis integration
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-validation` - Bean validation
- `spring-boot-starter-aop` - Aspect-oriented programming
- `spring-boot-starter-actuator` - Application monitoring

### Database & Drivers
- `h2` - In-memory database (development)
- `postgresql` - Production database

### Utilities
- `lombok` - Code generation
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` - JWT token handling
- `micrometer-registry-prometheus` - Metrics collection

## 🔧 Configuration

### Application Properties
```properties
# H2 Database (Development)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=root

# Admin Credentials (use environment variables in production)
admin.username=${ADMIN_USERNAME:adminUsername}
admin.password=${ADMIN_PASSWORD:adminPassword}

# Redis Configuration
spring.application.name=redis-employee-management
spring.redis.host=RedisHost
spring.redis.port=6379
spring.redis.database=0

# JWT Secret (use environment variable in production)
jwt.secret=${JWT_SECRET:my-secret-key}

# Actuator Endpoints
management.endpoints.web.exposure.include=health,metrics,prometheus,info

# Metrics Configuration
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.distribution.percentiles.http.server.requests=0.5,0.9,0.95,0.99
```

### Git Ignore Configuration
The project includes a comprehensive `.gitignore` file that excludes:
- **Build artifacts**: Maven/Gradle build directories, compiled classes
- **IDE files**: IntelliJ IDEA, Eclipse, VS Code, NetBeans configurations
- **Runtime files**: Logs, database files, Redis dumps
- **Environment files**: `.env` files and application profiles
- **System files**: OS-specific files (Windows/Mac)
- **Temporary files**: Editor backups, temporary files

**Key patterns**:
- `target/` - Maven build output
- `.idea/`, `.vscode/` - IDE configurations
- `*.log` - Log files
- `application-*.properties` - Environment-specific configs (except main)
- `.env*` - Environment variables
- `dump.rdb` - Redis database dumps

## 📊 API Endpoints

### Authentication Endpoints
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login (returns JWT token)
- `GET /api/users` - Get all users (ADMIN only)

### Employee Endpoints
- `GET /api/employees/{id}` - Get employee by ID
- `GET /api/employees/userId/{id}` - Get employee with rate limiting
- `GET /api/employees` - Get all employees
- `POST /api/employees` - Create new employee
- `PATCH /api/employees/{id}` - Partial update employee
- `PUT /api/employees/{id}` - Replace employee
- `DELETE /api/employees/{id}` - Delete employee

### Monitoring Endpoints
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics
- `/actuator/info` - Application information
- `/h2-console` - H2 database console (development)

## 🔐 Security Features

### JWT Authentication
- Token-based stateless authentication
- 1-hour token expiration
- HMAC-SHA256 signing algorithm
- Custom JWT filter for request validation

### Role-Based Access Control
- `ROLE_USER` - Standard user access
- `ROLE_ADMIN` - Administrative access
- Method-level security with `@PreAuthorize`

### Rate Limiting
- Redis-based sliding window algorithm
- 5 requests per minute per user
- Custom rate limiting headers support
- TTL information in error responses

## 💾 Caching Strategy

### Cache Annotations
- `@Cacheable` - Cache read operations
- `@CachePut` - Update cache entries
- `@CacheEvict` - Remove cache entries
- `@Caching` - Multiple cache operations

### Cache Configuration
- **Employee Cache**: Individual employee records by ID
- **Employees Cache**: Complete employee list
- **Cache Eviction**: Automatic cache invalidation on updates/deletes

## 📈 Monitoring & Observability

### AOP Execution Time Logging
- Automatic method execution time tracking
- Service layer monitoring
- Performance metrics logging

### Actuator Integration
- Health checks
- HTTP request metrics
- Prometheus metrics export
- Custom application information

## 🚨 Exception Handling

### Custom Exceptions
- `EmployeeNotFoundException` - Employee not found
- `EmployeeAlreadyExistsException` - Duplicate employee
- `UserNotFoundException` - User not found
- `UserAlreadyExistsException` - Duplicate user

### Global Exception Handler
- Centralized error handling
- HTTP status code mapping
- Detailed error responses
- Validation error handling

## 🔄 Data Flow

### Employee Operations
1. **Request**: HTTP request with JWT authentication
2. **Authentication**: JWT filter validates token
3. **Rate Limiting**: Redis rate limiter checks limits
4. **Service Layer**: Business logic execution
5. **Caching**: Redis cache interaction
6. **Database**: JPA repository operations
7. **Response**: Formatted JSON response

### User Authentication Flow
1. **Registration**: User creation with encrypted password
2. **Login**: Credential validation and JWT generation
3. **Authentication**: JWT token validation on protected endpoints
4. **Authorization**: Role-based access control

## 🎯 Performance Optimizations

### Redis Caching
- Read-through caching for employee data
- Write-through caching for data consistency
- Cache invalidation on data modifications
- Distributed caching support

### Rate Limiting
- Redis-based distributed rate limiting
- Sliding window algorithm
- Configurable limits and time windows
- Graceful degradation

### Database Optimization
- JPA query optimization
- Connection pooling
- Index-based queries
- Batch operations support

## 🔍 Development Tools

### H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `root`

### Actuator Endpoints
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Prometheus: `http://localhost:8080/actuator/prometheus`

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- Redis Server
- PostgreSQL (production)

### Running the Application
1. Start Redis server
2. Run the application: `mvn spring-boot:run`
3. Access the application at `http://localhost:8080`

### Default Admin User
- Username: `admin`
- Password: `admin@123`

## 📝 Usage Examples

### Register User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

### Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{"employeeId":"EMP001","name":"John Doe","role":"Developer","salary":75000.0}'
```

### Get Employee with Rate Limiting
```bash
curl -X GET http://localhost:8080/api/employees/userId/1 \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "X-Users-Id: user123"
```

## 🔧 Configuration Notes

### Environment Variables
- `JWT_SECRET` - JWT signing secret (required for production)
- `ADMIN_USERNAME` - Admin username override
- `ADMIN_PASSWORD` - Admin password override
- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SPRING_REDIS_HOST` - Redis server host
- `SPRING_REDIS_PASSWORD` - Redis password

### 🔐 Security Notice
**Never commit sensitive configuration files to version control!**

This project uses environment variables for sensitive data with fallback defaults:
```properties
# Example from application.properties
jwt.secret=${JWT_SECRET:default-secret-for-development-only}
admin.username=${ADMIN_USERNAME:admin}
admin.password=${ADMIN_PASSWORD:admin@123}
```

**Configuration Strategy**:
- **Development**: Uses default values shown in examples
- **Production**: Override with environment variables
- **Template files**: Use `application-*.properties.template` files

**Example Environment Setup**:
```bash
export JWT_SECRET="your-super-secure-jwt-secret-key"
export ADMIN_USERNAME="your-admin-user"
export ADMIN_PASSWORD="your-secure-password"
export SPRING_DATASOURCE_URL="jdbc:postgresql://prod-db:5432/employees"
export SPRING_DATASOURCE_USERNAME="prod-user"
export SPRING_DATASOURCE_PASSWORD="prod-password"
```

**Setup Instructions**:
1. Copy appropriate template: `cp application-prod.properties.template application-prod.properties`
2. Fill in actual values or use environment variables
3. Ensure `.gitignore` excludes actual configuration files
4. Use environment variables or secure vaults for production secrets

### Production Considerations
- Use PostgreSQL instead of H2
- Configure Redis cluster for high availability
- Use environment variables for sensitive data
- Enable SSL/TLS for production endpoints
- Configure proper logging levels
- Set up monitoring and alerting

## 📊 Metrics Collected

### HTTP Metrics
- Request count by status
- Request duration percentiles
- Active request count
- HTTP method distribution

### Application Metrics
- Method execution times
- Cache hit/miss ratios
- Rate limiting statistics
- Database query performance

## 🛡️ Security Best Practices

- JWT token expiration handling
- Password encryption with BCrypt
- Role-based access control
- Rate limiting to prevent abuse
- Input validation and sanitization
- Secure headers configuration
- CORS policy configuration

## 🔄 Future Enhancements

- Email notifications
- Audit logging
- Data export functionality
- Advanced search and filtering
- Bulk operations
- File upload support
- Integration with external systems
- Advanced caching strategies
- Distributed tracing

---

**Version**: 0.0.1-SNAPSHOT  
**Java Version**: 17  
**Spring Boot Version**: 3.5.10  
**Build Tool**: Maven
