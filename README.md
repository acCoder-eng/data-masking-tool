# Data Masking Tool - Full Stack Application

🔒 **GDPR/KVKK Compliant Data Masking & Anonymization Tool**

A comprehensive full-stack application for masking and anonymizing Personally Identifiable Information (PII) data. Built with Spring Boot backend and Next.js frontend.

## 🏗️ Architecture

```
📁 data-masking-tool/          # Spring Boot Backend
├── src/main/java/             # Java source code
├── src/main/resources/        # Configuration files
└── target/                    # Compiled classes

📁 frontend/                   # Next.js Frontend
├── src/app/                   # Next.js App Router pages
├── src/components/            # React components
├── src/lib/                   # Utility functions
└── public/                    # Static assets
```

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- Maven 3.6 or higher

### 1. Start Backend (Spring Boot)
```bash
cd data-masking-tool
mvn spring-boot:run
```
Backend will be available at: `http://localhost:8080`

### 2. Start Frontend (Next.js)
```bash
cd frontend
npm install
npm run dev
```
Frontend will be available at: `http://localhost:3000`

## 🎯 Features

### Backend (Spring Boot)
- **RESTful API** - Complete CRUD operations
- **Multiple PII Types** - Email, Phone, TC Kimlik, Credit Card, etc.
- **Various Masking Strategies** - Asterisk, Random, Hash, Placeholder, etc.
- **Rule Management** - Create and manage masking rules
- **Security** - Basic Authentication, CSRF Protection
- **Database** - H2 (development), MySQL/PostgreSQL (production)
- **Documentation** - Swagger/OpenAPI 3

### Frontend (Next.js)
- **Modern UI** - Clean, responsive design with Tailwind CSS
- **Data Masking Form** - Interactive form with real-time results
- **Rule Management** - CRUD operations for masking rules
- **Analytics Dashboard** - Statistics and compliance metrics
- **API Documentation** - Interactive API reference
- **TypeScript** - Full type safety

## 📱 Pages

### 1. Dashboard (`/`)
- Overview of the application
- Quick start guide
- Feature highlights
- Statistics

### 2. Mask Data (`/mask`)
- Interactive form for masking data
- Real-time results
- Copy to clipboard functionality
- Support for all PII types and strategies

### 3. Rules Management (`/rules`)
- Create, read, update, delete masking rules
- Visual rule configuration
- Status management (active/inactive)

### 4. Analytics (`/reports`)
- PII type distribution
- Strategy usage statistics
- Success rates and performance metrics
- Compliance scores

### 5. API Documentation (`/api-docs`)
- Complete API reference
- Interactive examples
- cURL commands
- Request/response schemas

## 🔧 API Endpoints

### Core Endpoints
- `POST /api/v1/masking/mask` - Mask single data
- `GET /api/v1/masking/pii-types` - Get PII types
- `GET /api/v1/masking/strategies` - Get strategies
- `GET /api/v1/masking/health` - Health check

### Rule Management
- `GET /api/v1/masking/rules` - Get all rules
- `POST /api/v1/masking/rules` - Create rule
- `PUT /api/v1/masking/rules/{id}` - Update rule
- `DELETE /api/v1/masking/rules/{id}` - Delete rule

## 🎨 UI Components

### Navigation
- Responsive navigation bar
- Active page highlighting
- Mobile-friendly menu

### Forms
- Real-time validation
- Dynamic form fields
- Error handling
- Loading states

### Tables
- Sortable columns
- Action buttons
- Status indicators
- Empty states

## 🔒 Security Features

- **Authentication** - Basic Auth (admin/admin123)
- **CSRF Protection** - Cross-site request forgery prevention
- **Input Validation** - Comprehensive request validation
- **Error Handling** - Secure error responses
- **Audit Logging** - Track all operations

## 📊 Supported PII Types

- 📧 **Email Addresses**
- 📞 **Phone Numbers**
- 🆔 **TC Kimlik No**
- 💳 **Credit Card Numbers**
- 👤 **Names** (Full, First, Last)
- 🏠 **Addresses**
- 📅 **Date of Birth**
- 🌐 **IP Addresses**
- 🏦 **Bank Account Numbers**
- 📄 **Passport Numbers**
- 🚗 **Driver's License Numbers**

## 🛠️ Masking Strategies

1. **ASTERISK** - Replace with asterisks (*)
2. **RANDOM** - Replace with random characters
3. **PLACEHOLDER** - Replace with fixed text
4. **HASH** - SHA-256 hash transformation
5. **NULLIFY** - Replace with null/empty
6. **PARTIAL** - Show first/last characters
7. **FORMAT_PRESERVING** - Maintain format

## 🚀 Deployment

### Backend Deployment
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/data-masking-tool-*.jar
```

### Frontend Deployment
```bash
# Build for production
npm run build

# Start production server
npm start
```

### Docker Deployment
```dockerfile
# Backend
FROM openjdk:17-jdk-slim
COPY target/data-masking-tool-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Frontend
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

## 🧪 Testing

### Backend Tests
```bash
cd data-masking-tool
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📚 Documentation

- **API Docs**: `http://localhost:8080/swagger-ui.html`
- **Database Console**: `http://localhost:8080/h2-console`
- **Frontend**: `http://localhost:3000`

## 🔧 Configuration

### Backend Configuration
```properties
# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# Security
spring.security.user.name=admin
spring.security.user.password=admin123
```

### Frontend Configuration
```javascript
// Environment variables
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_API_USERNAME=admin
NEXT_PUBLIC_API_PASSWORD=admin123
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Contact the development team

---

**Built with ❤️ for data privacy and compliance**

### Technology Stack
- **Backend**: Spring Boot 3.5.7, Java 17, Spring Data JPA, Spring Security
- **Frontend**: Next.js 14, React 18, TypeScript, Tailwind CSS
- **Database**: H2 (dev), MySQL/PostgreSQL (prod)
- **Tools**: Maven, npm, Swagger/OpenAPI 3
