package com.datamasking.tool.repository;

import com.datamasking.tool.model.MaskingRule;
import com.datamasking.tool.model.PiiType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for MaskingRule entity
 * Provides data access methods for masking rule configurations
 */
@Repository
public interface MaskingRuleRepository extends JpaRepository<MaskingRule, Long> {
    
    /**
     * Find masking rule by PII type
     */
    Optional<MaskingRule> findByPiiType(PiiType piiType);
    
    /**
     * Find active masking rules by PII type
     */
    @Query("SELECT r FROM MaskingRule r WHERE r.piiType = :piiType AND r.isActive = true")
    Optional<MaskingRule> findActiveByPiiType(@Param("piiType") PiiType piiType);
    
    /**
     * Find all active masking rules
     */
    @Query("SELECT r FROM MaskingRule r WHERE r.isActive = true ORDER BY r.piiType")
    List<MaskingRule> findAllActive();
    
    /**
     * Find masking rules by strategy
     */
    List<MaskingRule> findByStrategy(com.datamasking.tool.model.MaskingStrategy strategy);
    
    /**
     * Check if a masking rule exists for the given PII type
     */
    boolean existsByPiiType(PiiType piiType);
}
