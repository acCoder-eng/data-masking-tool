package com.datamasking.tool.model;

/**
 * Enum representing different data masking strategies
 * Each strategy defines how PII data should be anonymized
 */
public enum MaskingStrategy {
    
    /**
     * Replace with asterisks (*) - most common approach
     * Example: john.doe@email.com -> ***@***.com
     */
    ASTERISK,
    
    /**
     * Replace with random characters of same type
     * Example: john.doe@email.com -> xyz.abc@test.com
     */
    RANDOM,
    
    /**
     * Replace with fixed placeholder
     * Example: john.doe@email.com -> [EMAIL_MASKED]
     */
    PLACEHOLDER,
    
    /**
     * Hash the value (one-way transformation)
     * Example: john.doe@email.com -> a1b2c3d4e5f6...
     */
    HASH,
    
    /**
     * Replace with null or empty string
     */
    NULLIFY,
    
    /**
     * Partial masking - show first/last characters
     * Example: 1234567890 -> 123****890
     */
    PARTIAL,
    
    /**
     * Format-preserving encryption
     * Example: john.doe@email.com -> kpzq.efg@email.com
     */
    FORMAT_PRESERVING
}
