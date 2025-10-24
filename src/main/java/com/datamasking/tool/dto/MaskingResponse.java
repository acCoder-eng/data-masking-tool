package com.datamasking.tool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for masking response
 * Contains the masked data and metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaskingResponse {
    
    private String originalData;
    private String maskedData;
    private String piiType;
    private String strategy;
    private LocalDateTime processedAt;
    private Boolean success;
    private String errorMessage;
    
    // For batch processing
    private Map<String, Object> batchResults;
    private Long totalProcessed;
    private Long totalFailed;
    private String jobId;
}
