package com.datamasking.tool.dto;

import com.datamasking.tool.model.DatabaseConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for database configuration request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConfigRequest {
    
    @NotBlank(message = "Configuration name is required")
    private String configName;
    
    @NotNull(message = "Database type is required")
    private DatabaseConfig.DatabaseType databaseType;
    
    @NotBlank(message = "Host is required")
    private String host;
    
    @NotNull(message = "Port is required")
    @Positive(message = "Port must be positive")
    private Integer port;
    
    @NotBlank(message = "Database name is required")
    private String databaseName;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private String schema;
    private String description;
    private Boolean isDefault = false;
}
