package com.datamasking.tool.config;

import com.datamasking.tool.model.MaskingRule;
import com.datamasking.tool.model.MaskingStrategy;
import com.datamasking.tool.model.PiiType;
import com.datamasking.tool.repository.MaskingRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data seeder for initial masking rules
 * Creates default masking configurations for common PII types
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
    
    private final MaskingRuleRepository maskingRuleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding initial masking rules...");
        
        // Email masking rule
        createMaskingRuleIfNotExists(
            PiiType.EMAIL,
            MaskingStrategy.ASTERISK,
            "Mask email addresses with asterisks while preserving domain",
            true,
            true
        );
        
        // Phone masking rule
        createMaskingRuleIfNotExists(
            PiiType.PHONE,
            MaskingStrategy.ASTERISK,
            "Mask phone numbers with asterisks while preserving format",
            true,
            true
        );
        
        // TC Kimlik No masking rule
        createMaskingRuleIfNotExists(
            PiiType.TC_KIMLIK_NO,
            MaskingStrategy.ASTERISK,
            "Mask Turkish National ID with asterisks, show first 3 and last 3 digits",
            true,
            true
        );
        
        // Credit card masking rule
        createMaskingRuleIfNotExists(
            PiiType.CREDIT_CARD,
            MaskingStrategy.ASTERISK,
            "Mask credit card numbers, show last 4 digits",
            true,
            true
        );
        
        // Name masking rule
        createMaskingRuleIfNotExists(
            PiiType.FULL_NAME,
            MaskingStrategy.ASTERISK,
            "Mask full names with asterisks, show first character",
            true,
            false
        );
        
        // Address masking rule
        createMaskingRuleIfNotExists(
            PiiType.ADDRESS,
            MaskingStrategy.PLACEHOLDER,
            "Replace addresses with placeholder",
            false,
            false,
            "[ADDRESS_MASKED]"
        );
        
        // Date of birth masking rule
        createMaskingRuleIfNotExists(
            PiiType.DATE_OF_BIRTH,
            MaskingStrategy.PLACEHOLDER,
            "Replace date of birth with placeholder",
            false,
            false,
            "[DOB_MASKED]"
        );
        
        // IP address masking rule
        createMaskingRuleIfNotExists(
            PiiType.IP_ADDRESS,
            MaskingStrategy.HASH,
            "Hash IP addresses for anonymization",
            false,
            false
        );
        
        log.info("Initial masking rules seeded successfully!");
    }
    
    private void createMaskingRuleIfNotExists(PiiType piiType, MaskingStrategy strategy, 
                                            String description, Boolean preserveLength, 
                                            Boolean preserveFormat) {
        createMaskingRuleIfNotExists(piiType, strategy, description, preserveLength, preserveFormat, null);
    }
    
    private void createMaskingRuleIfNotExists(PiiType piiType, MaskingStrategy strategy, 
                                            String description, Boolean preserveLength, 
                                            Boolean preserveFormat, String replacementValue) {
        
        if (!maskingRuleRepository.existsByPiiType(piiType)) {
            MaskingRule rule = new MaskingRule();
            rule.setPiiType(piiType);
            rule.setStrategy(strategy);
            rule.setDescription(description);
            rule.setPreserveLength(preserveLength);
            rule.setPreserveFormat(preserveFormat);
            rule.setReplacementValue(replacementValue);
            rule.setIsActive(true);
            
            maskingRuleRepository.save(rule);
            log.info("Created masking rule for PII type: {}", piiType);
        } else {
            log.info("Masking rule for PII type {} already exists, skipping...", piiType);
        }
    }
}
