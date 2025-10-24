package com.datamasking.tool.model;

/**
 * Enum representing different types of Personally Identifiable Information (PII)
 * Each type has specific masking requirements and regulations
 */
public enum PiiType {
    
    /**
     * Email addresses
     * Example: john.doe@company.com
     */
    EMAIL,
    
    /**
     * Phone numbers (various formats)
     * Example: +90 555 123 4567, (555) 123-4567
     */
    PHONE,
    
    /**
     * Turkish National ID (TC Kimlik No)
     * Example: 12345678901
     */
    TC_KIMLIK_NO,
    
    /**
     * Credit card numbers
     * Example: 4532 1234 5678 9012
     */
    CREDIT_CARD,
    
    /**
     * Social Security Numbers
     * Example: 123-45-6789
     */
    SSN,
    
    /**
     * Physical addresses
     * Example: 123 Main St, Istanbul, Turkey
     */
    ADDRESS,
    
    /**
     * Full names
     * Example: John Doe, Ahmet Yılmaz
     */
    FULL_NAME,
    
    /**
     * First names
     * Example: John, Ahmet
     */
    FIRST_NAME,
    
    /**
     * Last names
     * Example: Doe, Yılmaz
     */
    LAST_NAME,
    
    /**
     * Date of birth
     * Example: 1990-01-15, 15/01/1990
     */
    DATE_OF_BIRTH,
    
    /**
     * IP addresses
     * Example: 192.168.1.1, 2001:db8::1
     */
    IP_ADDRESS,
    
    /**
     * Bank account numbers
     * Example: TR12 0006 4000 0011 2345 6789 01
     */
    BANK_ACCOUNT,
    
    /**
     * Passport numbers
     * Example: A1234567
     */
    PASSPORT,
    
    /**
     * Driver's license numbers
     * Example: D123456789
     */
    DRIVERS_LICENSE,
    
    /**
     * Generic text that may contain PII
     */
    TEXT,
    
    /**
     * Numeric data that may be sensitive
     */
    NUMERIC
}
