package com.datamasking.tool.dto;

import com.datamasking.tool.model.MaskingStrategy;
import com.datamasking.tool.model.PiiType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * DTO for masking request
 * Contains the data to be masked and configuration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaskingRequest {
    
    @NotBlank(message = "Data to mask cannot be blank")
    private String data;
    
    @NotNull(message = "PII type is required")
    private PiiType piiType;
    
    @NotNull(message = "Masking strategy is required")
    private MaskingStrategy strategy;
    
    private String customPattern;
    private String replacementValue;
    private Boolean preserveLength = true;
    private Boolean preserveFormat = true;
    
    // For batch processing
    private Map<String, Object> batchData;
    private String sourceTable;
    private String targetTable;
}
