package com.datamasking.tool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entity representing a data masking job
 * Tracks the execution of masking operations
 */
@Entity
@Table(name = "masking_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaskingJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_name", nullable = false)
    private String jobName;
    
    @Column(name = "source_table")
    private String sourceTable;
    
    @Column(name = "target_table")
    private String targetTable;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private JobStatus status = JobStatus.PENDING;
    
    @Column(name = "total_records")
    private Long totalRecords;
    
    @Column(name = "processed_records")
    private Long processedRecords = 0L;
    
    @Column(name = "failed_records")
    private Long failedRecords = 0L;
    
    @Column(name = "configuration", columnDefinition = "TEXT")
    private String configuration; // JSON string of masking rules
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum JobStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}
