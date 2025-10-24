package com.datamasking.tool.service;

import com.datamasking.tool.dto.MaskingRequest;
import com.datamasking.tool.dto.MaskingResponse;
import com.datamasking.tool.model.MaskingRule;
import com.datamasking.tool.model.PiiType;
import com.datamasking.tool.repository.MaskingRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service for data masking operations
 * Implements various masking strategies for different PII types
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MaskingService {
    
    private final MaskingRuleRepository maskingRuleRepository;
    
    // Regex patterns for different PII types
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    /**
     * Mask a single data value
     */
    public MaskingResponse maskData(MaskingRequest request) {
        try {
            log.info("Masking data of type: {} with strategy: {}", request.getPiiType(), request.getStrategy());
            
            String maskedData = applyMaskingStrategy(
                request.getData(), 
                request.getPiiType(), 
                request.getStrategy(),
                request.getCustomPattern(),
                request.getReplacementValue(),
                request.getPreserveLength(),
                request.getPreserveFormat()
            );
            
            return MaskingResponse.builder()
                .originalData(request.getData())
                .maskedData(maskedData)
                .piiType(request.getPiiType().name())
                .strategy(request.getStrategy().name())
                .processedAt(LocalDateTime.now())
                .success(true)
                .build();
                
        } catch (Exception e) {
            log.error("Error masking data: {}", e.getMessage(), e);
            return MaskingResponse.builder()
                .originalData(request.getData())
                .maskedData(null)
                .piiType(request.getPiiType().name())
                .strategy(request.getStrategy().name())
                .processedAt(LocalDateTime.now())
                .success(false)
                .errorMessage(e.getMessage())
                .build();
        }
    }
    
    /**
     * Apply masking strategy to data
     */
    private String applyMaskingStrategy(String data, PiiType piiType, 
                                        com.datamasking.tool.model.MaskingStrategy strategy,
                                        String customPattern, String replacementValue,
                                        Boolean preserveLength, Boolean preserveFormat) {
        
        if (StringUtils.isBlank(data)) {
            return data;
        }
        
        return switch (strategy) {
            case ASTERISK -> maskWithAsterisks(data, piiType, preserveLength, preserveFormat);
            case RANDOM -> maskWithRandom(data, piiType, preserveLength, preserveFormat);
            case PLACEHOLDER -> maskWithPlaceholder(data, piiType, replacementValue);
            case HASH -> maskWithHash(data);
            case NULLIFY -> null;
            case PARTIAL -> maskPartially(data, piiType, preserveLength);
            case FORMAT_PRESERVING -> maskFormatPreserving(data, piiType, preserveFormat);
        };
    }
    
    /**
     * Mask with asterisks (*)
     */
    private String maskWithAsterisks(String data, PiiType piiType, Boolean preserveLength, Boolean preserveFormat) {
        return switch (piiType) {
            case EMAIL -> maskEmailWithAsterisks(data, preserveFormat);
            case PHONE -> maskPhoneWithAsterisks(data, preserveFormat);
            case TC_KIMLIK_NO -> maskTcWithAsterisks(data);
            case CREDIT_CARD -> maskCreditCardWithAsterisks(data);
            case FULL_NAME, FIRST_NAME, LAST_NAME -> maskNameWithAsterisks(data);
            case ADDRESS -> maskAddressWithAsterisks(data);
            default -> StringUtils.repeat("*", preserveLength ? data.length() : 8);
        };
    }
    
    /**
     * Mask with random characters
     */
    private String maskWithRandom(String data, PiiType piiType, Boolean preserveLength, Boolean preserveFormat) {
        return switch (piiType) {
            case EMAIL -> maskEmailWithRandom(data, preserveFormat);
            case PHONE -> maskPhoneWithRandom(data, preserveFormat);
            case TC_KIMLIK_NO -> maskTcWithRandom(data);
            case CREDIT_CARD -> maskCreditCardWithRandom(data);
            case FULL_NAME, FIRST_NAME, LAST_NAME -> maskNameWithRandom(data);
            case ADDRESS -> maskAddressWithRandom(data);
            default -> generateRandomString(preserveLength ? data.length() : 8);
        };
    }
    
    /**
     * Mask with placeholder
     */
    private String maskWithPlaceholder(String data, PiiType piiType, String replacementValue) {
        if (StringUtils.isNotBlank(replacementValue)) {
            return replacementValue;
        }
        return "[" + piiType.name() + "_MASKED]";
    }
    
    /**
     * Mask with hash
     */
    private String maskWithHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available", e);
            return "HASH_ERROR";
        }
    }
    
    /**
     * Partial masking
     */
    private String maskPartially(String data, PiiType piiType, Boolean preserveLength) {
        if (data.length() <= 2) {
            return StringUtils.repeat("*", data.length());
        }
        
        int visibleChars = Math.max(1, data.length() / 4);
        String start = data.substring(0, visibleChars);
        String end = data.substring(data.length() - visibleChars);
        String middle = StringUtils.repeat("*", data.length() - (2 * visibleChars));
        
        return start + middle + end;
    }
    
    /**
     * Format preserving masking
     */
    private String maskFormatPreserving(String data, PiiType piiType, Boolean preserveFormat) {
        // This is a simplified version - in production, you'd use proper format-preserving encryption
        return maskWithRandom(data, piiType, true, preserveFormat);
    }
    
    // Specific masking methods for different PII types
    
    private String maskEmailWithAsterisks(String email, Boolean preserveFormat) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return StringUtils.repeat("*", email.length());
        }
        
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        
        if (preserveFormat) {
            String maskedLocal = localPart.length() > 2 ? 
                localPart.charAt(0) + StringUtils.repeat("*", localPart.length() - 2) + localPart.charAt(localPart.length() - 1) :
                StringUtils.repeat("*", localPart.length());
            return maskedLocal + "@" + domain;
        } else {
            return "***@" + domain;
        }
    }
    
    private String maskEmailWithRandom(String email, Boolean preserveFormat) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return generateRandomString(email.length());
        }
        
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        
        if (preserveFormat) {
            String randomLocal = generateRandomString(localPart.length());
            return randomLocal + "@" + domain;
        } else {
            return generateRandomString(8) + "@" + domain;
        }
    }
    
    private String maskPhoneWithAsterisks(String phone, Boolean preserveFormat) {
        if (preserveFormat) {
            return phone.replaceAll("[0-9]", "*");
        } else {
            return StringUtils.repeat("*", phone.length());
        }
    }
    
    private String maskPhoneWithRandom(String phone, Boolean preserveFormat) {
        if (preserveFormat) {
            return phone.replaceAll("[0-9]", "X");
        } else {
            return generateRandomString(phone.length());
        }
    }
    
    private String maskTcWithAsterisks(String tc) {
        if (tc.length() == 11) {
            return tc.substring(0, 3) + "****" + tc.substring(7);
        }
        return StringUtils.repeat("*", tc.length());
    }
    
    private String maskTcWithRandom(String tc) {
        if (tc.length() == 11) {
            return tc.substring(0, 3) + generateRandomNumeric(4) + tc.substring(7);
        }
        return generateRandomNumeric(tc.length());
    }
    
    private String maskCreditCardWithAsterisks(String card) {
        String cleaned = card.replaceAll("[^0-9]", "");
        if (cleaned.length() >= 4) {
            return StringUtils.repeat("*", cleaned.length() - 4) + cleaned.substring(cleaned.length() - 4);
        }
        return StringUtils.repeat("*", card.length());
    }
    
    private String maskCreditCardWithRandom(String card) {
        String cleaned = card.replaceAll("[^0-9]", "");
        if (cleaned.length() >= 4) {
            return generateRandomNumeric(cleaned.length() - 4) + cleaned.substring(cleaned.length() - 4);
        }
        return generateRandomNumeric(card.length());
    }
    
    private String maskNameWithAsterisks(String name) {
        if (name.length() <= 2) {
            return StringUtils.repeat("*", name.length());
        }
        return name.charAt(0) + StringUtils.repeat("*", name.length() - 1);
    }
    
    private String maskNameWithRandom(String name) {
        return generateRandomString(name.length());
    }
    
    private String maskAddressWithAsterisks(String address) {
        return StringUtils.repeat("*", address.length());
    }
    
    private String maskAddressWithRandom(String address) {
        return generateRandomString(address.length());
    }
    
    // Utility methods
    
    private String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
    
    private String generateRandomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }
    
    /**
     * Get default masking rule for PII type
     */
    public Optional<MaskingRule> getDefaultRule(PiiType piiType) {
        return maskingRuleRepository.findActiveByPiiType(piiType);
    }
}
