package com.datamasking.tool.controller;

import com.datamasking.tool.dto.DatabaseConfigRequest;
import com.datamasking.tool.model.DatabaseConfig;
import com.datamasking.tool.service.DatabaseConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for database configuration management
 */
@RestController
@RequestMapping("/api/v1/database-config")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Database Configuration", description = "API for managing database connections")
public class DatabaseConfigController {
    
    private final DatabaseConfigService databaseConfigService;
    
    /**
     * Create new database configuration
     */
    @PostMapping
    @Operation(summary = "Create database configuration", 
               description = "Create a new database connection configuration")
    public ResponseEntity<DatabaseConfig> createConfig(
            @Parameter(description = "Database configuration details")
            @Valid @RequestBody DatabaseConfigRequest request) {
        
        log.info("Creating database configuration: {}", request.getConfigName());
        
        try {
            DatabaseConfig config = databaseConfigService.createConfig(request);
            return ResponseEntity.ok(config);
        } catch (IllegalArgumentException e) {
            log.error("Error creating database configuration: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all database configurations
     */
    @GetMapping
    @Operation(summary = "Get all database configurations", 
               description = "Returns list of all database configurations")
    public ResponseEntity<List<DatabaseConfig>> getAllConfigs() {
        List<DatabaseConfig> configs = databaseConfigService.getAllConfigs();
        return ResponseEntity.ok(configs);
    }
    
    /**
     * Get active database configuration
     */
    @GetMapping("/active")
    @Operation(summary = "Get active database configuration", 
               description = "Returns the currently active database configuration")
    public ResponseEntity<DatabaseConfig> getActiveConfig() {
        Optional<DatabaseConfig> config = databaseConfigService.getActiveConfig();
        return config.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get default database configuration
     */
    @GetMapping("/default")
    @Operation(summary = "Get default database configuration", 
               description = "Returns the default database configuration")
    public ResponseEntity<DatabaseConfig> getDefaultConfig() {
        Optional<DatabaseConfig> config = databaseConfigService.getDefaultConfig();
        return config.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Test database connection
     */
    @PostMapping("/{id}/test")
    @Operation(summary = "Test database connection", 
               description = "Test the database connection for a specific configuration")
    public ResponseEntity<Map<String, Object>> testConnection(
            @Parameter(description = "Configuration ID to test")
            @PathVariable Long id) {
        
        boolean success = databaseConfigService.testConnection(id);
        
        Map<String, Object> response = Map.of(
            "success", success,
            "message", success ? "Connection successful" : "Connection failed",
            "configId", id
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Activate database configuration
     */
    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate database configuration", 
               description = "Activate a database configuration and deactivate others")
    public ResponseEntity<Map<String, Object>> activateConfig(
            @Parameter(description = "Configuration ID to activate")
            @PathVariable Long id) {
        
        boolean success = databaseConfigService.activateConfig(id);
        
        Map<String, Object> response = Map.of(
            "success", success,
            "message", success ? "Configuration activated successfully" : "Failed to activate configuration",
            "configId", id
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update database configuration
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update database configuration", 
               description = "Update an existing database configuration")
    public ResponseEntity<DatabaseConfig> updateConfig(
            @Parameter(description = "Configuration ID to update")
            @PathVariable Long id,
            @Parameter(description = "Updated configuration details")
            @Valid @RequestBody DatabaseConfigRequest request) {
        
        try {
            DatabaseConfig config = databaseConfigService.updateConfig(id, request);
            return ResponseEntity.ok(config);
        } catch (IllegalArgumentException e) {
            log.error("Error updating database configuration: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete database configuration
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete database configuration", 
               description = "Delete a database configuration")
    public ResponseEntity<Map<String, Object>> deleteConfig(
            @Parameter(description = "Configuration ID to delete")
            @PathVariable Long id) {
        
        boolean success = databaseConfigService.deleteConfig(id);
        
        Map<String, Object> response = Map.of(
            "success", success,
            "message", success ? "Configuration deleted successfully" : "Failed to delete configuration",
            "configId", id
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get available database types
     */
    @GetMapping("/database-types")
    @Operation(summary = "Get available database types", 
               description = "Returns list of supported database types")
    public ResponseEntity<List<Map<String, String>>> getDatabaseTypes() {
        List<Map<String, String>> types = List.of(
            Map.of("type", "MYSQL", "displayName", "MySQL"),
            Map.of("type", "POSTGRESQL", "displayName", "PostgreSQL"),
            Map.of("type", "H2", "displayName", "H2 Database"),
            Map.of("type", "ORACLE", "displayName", "Oracle Database"),
            Map.of("type", "SQLSERVER", "displayName", "SQL Server")
        );
        
        return ResponseEntity.ok(types);
    }
}
