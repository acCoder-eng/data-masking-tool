package com.datamasking.tool.repository;

import com.datamasking.tool.model.DatabaseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for DatabaseConfig entity
 */
@Repository
public interface DatabaseConfigRepository extends JpaRepository<DatabaseConfig, Long> {
    
    /**
     * Find by configuration name
     */
    Optional<DatabaseConfig> findByConfigName(String configName);
    
    /**
     * Find active configurations
     */
    @Query("SELECT d FROM DatabaseConfig d WHERE d.isActive = true")
    List<DatabaseConfig> findActiveConfigs();
    
    /**
     * Find default configuration
     */
    @Query("SELECT d FROM DatabaseConfig d WHERE d.isDefault = true")
    Optional<DatabaseConfig> findDefaultConfig();
    
    /**
     * Find by database type
     */
    List<DatabaseConfig> findByDatabaseType(DatabaseConfig.DatabaseType databaseType);
    
    /**
     * Check if configuration name exists
     */
    boolean existsByConfigName(String configName);
    
    /**
     * Find by host and port
     */
    @Query("SELECT d FROM DatabaseConfig d WHERE d.host = :host AND d.port = :port")
    List<DatabaseConfig> findByHostAndPort(@Param("host") String host, @Param("port") Integer port);
}
