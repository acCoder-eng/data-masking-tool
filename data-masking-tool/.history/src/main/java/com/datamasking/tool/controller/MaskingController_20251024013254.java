package com.datamasking.tool.controller;

import com.datamasking.tool.dto.MaskingRequest;
import com.datamasking.tool.dto.MaskingResponse;
import com.datamasking.tool.model.MaskingRule;
import com.datamasking.tool.model.PiiType;
import com.datamasking.tool.service.MaskingService;
import com.datamasking.tool.repository.MaskingRuleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for data masking operations
 * Provides endpoints for masking PII data
 */
@RestController
@RequestMapping("/api/v1/masking")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Data Masking", description = "API for masking and anonymizing PII data")
public class MaskingController {
    
    private final MaskingService maskingService;
    private final MaskingRuleRepository maskingRuleRepository;
    
    /**
     * Mask a single data value
     */
    @PostMapping("/mask")
    @Operation(summary = "Mask single data value", 
               description = "Apply masking strategy to a single PII data value")
    public ResponseEntity<MaskingResponse> maskData(
            @Parameter(description = "Masking request containing data and configuration")
            @Valid @RequestBody MaskingRequest request) {
        
        log.info("Received masking request for PII type: {}", request.getPiiType());
        
        MaskingResponse response = maskingService.maskData(request);
        
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get available PII types
     */
    @GetMapping("/pii-types")
    @Operation(summary = "Get available PII types", 
               description = "Returns list of supported PII types for masking")
    public ResponseEntity<List<String>> getPiiTypes() {
        List<String> piiTypes = List.of(PiiType.values())
            .stream()
            .map(Enum::name)
            .toList();
        
        return ResponseEntity.ok(piiTypes);
    }
    
    /**
     * Get masking strategies
     */
    @GetMapping("/strategies")
    @Operation(summary = "Get available masking strategies", 
               description = "Returns list of supported masking strategies")
    public ResponseEntity<List<String>> getMaskingStrategies() {
        List<String> strategies = List.of(com.datamasking.tool.model.MaskingStrategy.values())
            .stream()
            .map(Enum::name)
            .toList();
        
        return ResponseEntity.ok(strategies);
    }
    
    /**
     * Get masking rules
     */
    @GetMapping("/rules")
    @Operation(summary = "Get masking rules", 
               description = "Returns all configured masking rules")
    public ResponseEntity<List<MaskingRule>> getMaskingRules() {
        List<MaskingRule> rules = maskingRuleRepository.findAll();
        return ResponseEntity.ok(rules);
    }
    
    /**
     * Get masking rule by PII type
     */
    @GetMapping("/rules/{piiType}")
    @Operation(summary = "Get masking rule by PII type", 
               description = "Returns masking rule configuration for specific PII type")
    public ResponseEntity<MaskingRule> getMaskingRule(
            @Parameter(description = "PII type to get rule for")
            @PathVariable PiiType piiType) {
        
        Optional<MaskingRule> rule = maskingRuleRepository.findByPiiType(piiType);
        
        if (rule.isPresent()) {
            return ResponseEntity.ok(rule.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Create or update masking rule
     */
    @PostMapping("/rules")
    @Operation(summary = "Create masking rule", 
               description = "Create a new masking rule configuration")
    public ResponseEntity<MaskingRule> createMaskingRule(
            @Parameter(description = "Masking rule configuration")
            @Valid @RequestBody MaskingRule rule) {
        
        log.info("Creating masking rule for PII type: {}", rule.getPiiType());
        
        MaskingRule savedRule = maskingRuleRepository.save(rule);
        return ResponseEntity.ok(savedRule);
    }
    
    /**
     * Update masking rule
     */
    @PutMapping("/rules/{id}")
    @Operation(summary = "Update masking rule", 
               description = "Update existing masking rule configuration")
    public ResponseEntity<MaskingRule> updateMaskingRule(
            @Parameter(description = "Rule ID to update")
            @PathVariable Long id,
            @Parameter(description = "Updated masking rule configuration")
            @Valid @RequestBody MaskingRule rule) {
        
        if (!maskingRuleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        rule.setId(id);
        MaskingRule updatedRule = maskingRuleRepository.save(rule);
        
        log.info("Updated masking rule with ID: {}", id);
        return ResponseEntity.ok(updatedRule);
    }
    
    /**
     * Delete masking rule
     */
    @DeleteMapping("/rules/{id}")
    @Operation(summary = "Delete masking rule", 
               description = "Delete masking rule configuration")
    public ResponseEntity<Void> deleteMaskingRule(
            @Parameter(description = "Rule ID to delete")
            @PathVariable Long id) {
        
        if (!maskingRuleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        maskingRuleRepository.deleteById(id);
        log.info("Deleted masking rule with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", 
               description = "Check if the masking service is running")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "service", "Data Masking Tool",
            "timestamp", java.time.LocalDateTime.now()
        );
        
        return ResponseEntity.ok(health);
    }
}
