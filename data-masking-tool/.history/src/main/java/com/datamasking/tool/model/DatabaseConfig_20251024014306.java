package com.datamasking.tool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing database configuration
 * Stores database connection settings
 */
@Entity
@Table(name = "database_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_name", nullable = false, unique = true)
    private String configName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "database_type", nullable = false)
    private DatabaseType databaseType;
    
    @Column(name = "host", nullable = false)
    private String host;
    
    @Column(name = "port", nullable = false)
    private Integer port;
    
    @Column(name = "database_name", nullable = false)
    private String databaseName;
    
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "schema")
    private String schema;
    
    @Column(name = "is_active")
    private Boolean isActive = false;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    @Column(name = "connection_url")
    private String connectionUrl;
    
    @Column(name = "driver_class")
    private String driverClass;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        generateConnectionUrl();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        generateConnectionUrl();
    }
    
    private void generateConnectionUrl() {
        switch (databaseType) {
            case MYSQL:
                this.connectionUrl = String.format("jdbc:mysql://%s:%d/%s", host, port, databaseName);
                this.driverClass = "com.mysql.cj.jdbc.Driver";
                break;
            case POSTGRESQL:
                this.connectionUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, databaseName);
                this.driverClass = "org.postgresql.Driver";
                break;
            case H2:
                this.connectionUrl = String.format("jdbc:h2:mem:%s", databaseName);
                this.driverClass = "org.h2.Driver";
                break;
            case ORACLE:
                this.connectionUrl = String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, databaseName);
                this.driverClass = "oracle.jdbc.OracleDriver";
                break;
            case SQLSERVER:
                this.connectionUrl = String.format("jdbc:sqlserver://%s:%d;databaseName=%s", host, port, databaseName);
                this.driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                break;
        }
    }
    
    public enum DatabaseType {
        MYSQL("MySQL"),
        POSTGRESQL("PostgreSQL"),
        H2("H2 Database"),
        ORACLE("Oracle Database"),
        SQLSERVER("SQL Server");
        
        private final String displayName;
        
        DatabaseType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
