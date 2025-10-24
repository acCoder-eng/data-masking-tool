# Data Masking & Anonymization Tool

🔒 **GDPR/KVKK Compliant Data Masking & Anonymization Tool**

A comprehensive Spring Boot application for masking and anonymizing Personally Identifiable Information (PII) data. Perfect for organizations that need to transfer production data to testing environments while maintaining privacy compliance.

## 🚀 Features

### Core Capabilities
- **Multiple PII Types**: Email, Phone, TC Kimlik No, Credit Card, Names, Addresses, and more
- **Various Masking Strategies**: Asterisk, Random, Hash, Placeholder, Partial, Format-Preserving
- **GDPR/KVKK Compliance**: Built with privacy regulations in mind
- **RESTful API**: Easy integration with existing systems
- **Configurable Rules**: Customize masking behavior per PII type
- **Batch Processing**: Handle large datasets efficiently

### Supported PII Types
- 📧 Email addresses
- 📞 Phone numbers (various formats)
- 🆔 Turkish National ID (TC Kimlik No)
- 💳 Credit card numbers
- 👤 Names (Full, First, Last)
- 🏠 Physical addresses
- 📅 Date of birth
- 🌐 IP addresses
- 🏦 Bank account numbers
- 📄 Passport numbers
- 🚗 Driver's license numbers

### Masking Strategies
- **Asterisk (*)**: Replace with asterisks while preserving format
- **Random**: Replace with random characters of same type
- **Placeholder**: Replace with configurable placeholder text
- **Hash**: One-way hash transformation
- **Nullify**: Replace with null/empty values
- **Partial**: Show first/last characters, mask middle
- **Format-Preserving**: Maintain original format with encrypted data

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (Development)
- **MySQL/PostgreSQL** (Production)
- **Swagger/OpenAPI 3**
- **Lombok**
- **Maven**

## 📦 Installation & Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Git

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd data-masking-tool
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 Console: `http://localhost:8080/h2-console`

### Default Credentials
- Username: `admin`
- Password: `admin123`

## 📚 API Documentation

### Core Endpoints

#### Mask Single Data
```http
POST /api/v1/masking/mask
Content-Type: application/json

{
  "data": "john.doe@example.com",
  "piiType": "EMAIL",
  "strategy": "ASTERISK",
  "preserveFormat": true
}
```

#### Get Available PII Types
```http
GET /api/v1/masking/pii-types
```

#### Get Masking Strategies
```http
GET /api/v1/masking/strategies
```

#### Manage Masking Rules
```http
GET /api/v1/masking/rules
POST /api/v1/masking/rules
PUT /api/v1/masking/rules/{id}
DELETE /api/v1/masking/rules/{id}
```

### Example Responses

#### Successful Masking Response
```json
{
  "originalData": "john.doe@example.com",
  "maskedData": "j***e@example.com",
  "piiType": "EMAIL",
  "strategy": "ASTERISK",
  "processedAt": "2024-01-15T10:30:00",
  "success": true
}
```

## 🔧 Configuration

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123

# Swagger Configuration
springdoc.swagger-ui.enabled=true
```

### Production Configuration
For production environments, update the database configuration:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/datamasking
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Test Coverage
```bash
mvn jacoco:report
```

## 📊 Usage Examples

### 1. Mask Email Address
```bash
curl -X POST http://localhost:8080/api/v1/masking/mask \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "data": "john.doe@example.com",
    "piiType": "EMAIL",
    "strategy": "ASTERISK"
  }'
```

### 2. Mask Turkish National ID
```bash
curl -X POST http://localhost:8080/api/v1/masking/mask \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "data": "12345678901",
    "piiType": "TC_KIMLIK_NO",
    "strategy": "ASTERISK"
  }'
```

### 3. Hash Sensitive Data
```bash
curl -X POST http://localhost:8080/api/v1/masking/mask \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "data": "sensitive information",
    "piiType": "TEXT",
    "strategy": "HASH"
  }'
```

## 🏗️ Architecture

### Layered Architecture
```
├── Controller Layer (REST API)
├── Service Layer (Business Logic)
├── Repository Layer (Data Access)
└── Model Layer (Entities & DTOs)
```

### Key Components
- **MaskingService**: Core masking logic
- **MaskingController**: REST API endpoints
- **MaskingRuleRepository**: Data access for rules
- **SecurityConfig**: Authentication & authorization
- **SwaggerConfig**: API documentation

## 🔒 Security Features

- **Basic Authentication**: Secure API access
- **CSRF Protection**: Cross-site request forgery prevention
- **Input Validation**: Comprehensive request validation
- **Error Handling**: Secure error responses
- **Audit Logging**: Track all masking operations

## 🚀 Deployment

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/data-masking-tool-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Production Considerations
- Use production database (MySQL/PostgreSQL)
- Configure proper security settings
- Set up monitoring and logging
- Implement backup strategies
- Use HTTPS in production

## 📈 Performance

- **High Throughput**: Optimized for batch processing
- **Memory Efficient**: Stream processing for large datasets
- **Scalable**: Horizontal scaling support
- **Caching**: Rule caching for better performance

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## 🔮 Roadmap

- [ ] Advanced format-preserving encryption
- [ ] Machine learning-based PII detection
- [ ] Real-time data streaming support
- [ ] Enhanced audit and compliance reporting
- [ ] Multi-tenant support
- [ ] Cloud deployment templates

---

**Built with ❤️ for data privacy and compliance**
