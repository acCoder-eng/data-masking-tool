package com.datamasking.tool.repository;

import com.datamasking.tool.model.MaskingJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for MaskingJob entity
 * Provides data access methods for masking job tracking
 */
@Repository
public interface MaskingJobRepository extends JpaRepository<MaskingJob, Long> {
    
    /**
     * Find jobs by status
     */
    List<MaskingJob> findByStatus(MaskingJob.JobStatus status);
    
    /**
     * Find jobs by creator
     */
    List<MaskingJob> findByCreatedBy(String createdBy);
    
    /**
     * Find jobs created between dates
     */
    @Query("SELECT j FROM MaskingJob j WHERE j.createdAt BETWEEN :startDate AND :endDate")
    List<MaskingJob> findJobsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find running jobs
     */
    @Query("SELECT j FROM MaskingJob j WHERE j.status = 'RUNNING'")
    List<MaskingJob> findRunningJobs();
    
    /**
     * Find jobs by source table
     */
    List<MaskingJob> findBySourceTable(String sourceTable);
    
    /**
     * Find jobs by target table
     */
    List<MaskingJob> findByTargetTable(String targetTable);
    
    /**
     * Count jobs by status
     */
    @Query("SELECT COUNT(j) FROM MaskingJob j WHERE j.status = :status")
    Long countByStatus(@Param("status") MaskingJob.JobStatus status);
}
