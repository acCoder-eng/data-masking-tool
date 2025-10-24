package com.datamasking.tool.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration for the Data Masking Tool
 * Provides API documentation and interactive testing interface
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI dataMaskingToolOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Data Masking & Anonymization Tool API")
                .description("""
                    GDPR/KVKK Compliant Data Masking & Anonymization Tool
                    
                    This API provides comprehensive data masking capabilities for:
                    - Email addresses
                    - Phone numbers
                    - Turkish National ID (TC Kimlik No)
                    - Credit card numbers
                    - Names and addresses
                    - And many more PII types
                    
                    Features:
                    - Multiple masking strategies (Asterisk, Random, Hash, etc.)
                    - Format-preserving encryption
                    - Batch processing capabilities
                    - Configurable masking rules
                    - GDPR/KVKK compliance
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("Data Masking Tool Team")
                    .email("support@datamasking.com")
                    .url("https://datamasking.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Development Server"),
                new Server()
                    .url("https://api.datamasking.com")
                    .description("Production Server")
            ));
    }
}
