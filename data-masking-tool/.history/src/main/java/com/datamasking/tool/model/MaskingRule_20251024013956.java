package com.datamasking.tool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a masking rule configuration
 * Defines how specific PII types should be masked
 */
@Entity
@Table(name = "masking_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaskingRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PiiType piiType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaskingStrategy strategy;
    
    @Column(name = "custom_pattern")
    private String customPattern;
    
    @Column(name = "replacement_value")
    private String replacementValue;
    
    @Column(name = "preserve_length")
    private Boolean preserveLength = true;
    
    @Column(name = "preserve_format")
    private Boolean preserveFormat = true;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
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
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
