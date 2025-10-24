package com.datamasking.tool.service;

import com.datamasking.tool.dto.DatabaseConfigRequest;
import com.datamasking.tool.model.DatabaseConfig;
import com.datamasking.tool.repository.DatabaseConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service for database configuration management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfigService {
    
    private final DatabaseConfigRepository databaseConfigRepository;
    
    /**
     * Create new database configuration
     */
    @Transactional
    public DatabaseConfig createConfig(DatabaseConfigRequest request) {
        log.info("Creating database configuration: {}", request.getConfigName());
        
        // Check if config name already exists
        if (databaseConfigRepository.existsByConfigName(request.getConfigName())) {
            throw new IllegalArgumentException("Configuration name already exists: " + request.getConfigName());
        }
        
        // If this is set as default, unset other defaults
        if (request.getIsDefault()) {
            unsetDefaultConfigs();
        }
        
        DatabaseConfig config = DatabaseConfig.builder()
            .configName(request.getConfigName())
            .databaseType(request.getDatabaseType())
            .host(request.getHost())
            .port(request.getPort())
            .databaseName(request.getDatabaseName())
            .username(request.getUsername())
            .password(request.getPassword())
            .schema(request.getSchema())
            .description(request.getDescription())
            .isDefault(request.getIsDefault())
            .isActive(false) // Start as inactive
            .build();
        
        return databaseConfigRepository.save(config);
    }
    
    /**
     * Test database connection
     */
    public boolean testConnection(Long configId) {
        Optional<DatabaseConfig> configOpt = databaseConfigRepository.findById(configId);
        if (configOpt.isEmpty()) {
            return false;
        }
        
        DatabaseConfig config = configOpt.get();
        
        try {
            Class.forName(config.getDriverClass());
            try (Connection connection = DriverManager.getConnection(
                    config.getConnectionUrl(),
                    config.getUsername(),
                    config.getPassword())) {
                
                log.info("Database connection test successful for config: {}", config.getConfigName());
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Database connection test failed for config: {}", config.getConfigName(), e);
            return false;
        }
    }
    
    /**
     * Activate database configuration
     */
    @Transactional
    public boolean activateConfig(Long configId) {
        Optional<DatabaseConfig> configOpt = databaseConfigRepository.findById(configId);
        if (configOpt.isEmpty()) {
            return false;
        }
        
        DatabaseConfig config = configOpt.get();
        
        // Test connection first
        if (!testConnection(configId)) {
            log.error("Cannot activate config {} - connection test failed", config.getConfigName());
            return false;
        }
        
        // Deactivate other configs
        deactivateAllConfigs();
        
        // Activate this config
        config.setIsActive(true);
        databaseConfigRepository.save(config);
        
        log.info("Activated database configuration: {}", config.getConfigName());
        return true;
    }
    
    /**
     * Get all configurations
     */
    public List<DatabaseConfig> getAllConfigs() {
        return databaseConfigRepository.findAll();
    }
    
    /**
     * Get active configuration
     */
    public Optional<DatabaseConfig> getActiveConfig() {
        return databaseConfigRepository.findActiveConfigs().stream().findFirst();
    }
    
    /**
     * Get default configuration
     */
    public Optional<DatabaseConfig> getDefaultConfig() {
        return databaseConfigRepository.findDefaultConfig();
    }
    
    /**
     * Delete configuration
     */
    @Transactional
    public boolean deleteConfig(Long configId) {
        Optional<DatabaseConfig> configOpt = databaseConfigRepository.findById(configId);
        if (configOpt.isEmpty()) {
            return false;
        }
        
        DatabaseConfig config = configOpt.get();
        if (config.getIsActive()) {
            log.warn("Cannot delete active configuration: {}", config.getConfigName());
            return false;
        }
        
        databaseConfigRepository.deleteById(configId);
        log.info("Deleted database configuration: {}", config.getConfigName());
        return true;
    }
    
    /**
     * Update configuration
     */
    @Transactional
    public DatabaseConfig updateConfig(Long configId, DatabaseConfigRequest request) {
        Optional<DatabaseConfig> configOpt = databaseConfigRepository.findById(configId);
        if (configOpt.isEmpty()) {
            throw new IllegalArgumentException("Configuration not found with ID: " + configId);
        }
        
        DatabaseConfig config = configOpt.get();
        
        // Check if config name is being changed and if it already exists
        if (!config.getConfigName().equals(request.getConfigName()) && 
            databaseConfigRepository.existsByConfigName(request.getConfigName())) {
            throw new IllegalArgumentException("Configuration name already exists: " + request.getConfigName());
        }
        
        // If this is set as default, unset other defaults
        if (request.getIsDefault() && !config.getIsDefault()) {
            unsetDefaultConfigs();
        }
        
        config.setConfigName(request.getConfigName());
        config.setDatabaseType(request.getDatabaseType());
        config.setHost(request.getHost());
        config.setPort(request.getPort());
        config.setDatabaseName(request.getDatabaseName());
        config.setUsername(request.getUsername());
        config.setPassword(request.getPassword());
        config.setSchema(request.getSchema());
        config.setDescription(request.getDescription());
        config.setIsDefault(request.getIsDefault());
        
        return databaseConfigRepository.save(config);
    }
    
    private void unsetDefaultConfigs() {
        List<DatabaseConfig> defaultConfigs = databaseConfigRepository.findAll()
            .stream()
            .filter(DatabaseConfig::getIsDefault)
            .toList();
        
        for (DatabaseConfig config : defaultConfigs) {
            config.setIsDefault(false);
            databaseConfigRepository.save(config);
        }
    }
    
    private void deactivateAllConfigs() {
        List<DatabaseConfig> activeConfigs = databaseConfigRepository.findActiveConfigs();
        for (DatabaseConfig config : activeConfigs) {
            config.setIsActive(false);
            databaseConfigRepository.save(config);
        }
    }
}
