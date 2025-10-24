package com.datamasking.tool.service;

import com.datamasking.tool.dto.MaskingRequest;
import com.datamasking.tool.dto.MaskingResponse;
import com.datamasking.tool.model.MaskingStrategy;
import com.datamasking.tool.model.PiiType;
import com.datamasking.tool.repository.MaskingRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for MaskingService
 */
@ExtendWith(MockitoExtension.class)
class MaskingServiceTest {
    
    @Mock
    private MaskingRuleRepository maskingRuleRepository;
    
    private MaskingService maskingService;
    
    @BeforeEach
    void setUp() {
        maskingService = new MaskingService(maskingRuleRepository);
    }
    
    @Test
    void testMaskEmailWithAsterisks() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("john.doe@example.com")
            .piiType(PiiType.EMAIL)
            .strategy(MaskingStrategy.ASTERISK)
            .preserveFormat(true)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertNotNull(response.getMaskedData());
        assertTrue(response.getMaskedData().contains("@"));
        assertTrue(response.getMaskedData().contains("example.com"));
        assertTrue(response.getMaskedData().contains("*"));
    }
    
    @Test
    void testMaskPhoneWithAsterisks() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("+90 555 123 4567")
            .piiType(PiiType.PHONE)
            .strategy(MaskingStrategy.ASTERISK)
            .preserveFormat(true)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertNotNull(response.getMaskedData());
        assertTrue(response.getMaskedData().contains("*"));
    }
    
    @Test
    void testMaskTcWithAsterisks() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("12345678901")
            .piiType(PiiType.TC_KIMLIK_NO)
            .strategy(MaskingStrategy.ASTERISK)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertNotNull(response.getMaskedData());
        assertEquals(11, response.getMaskedData().length());
        assertTrue(response.getMaskedData().startsWith("123"));
        assertTrue(response.getMaskedData().endsWith("901"));
        assertTrue(response.getMaskedData().contains("****"));
    }
    
    @Test
    void testMaskWithHash() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("sensitive data")
            .piiType(PiiType.TEXT)
            .strategy(MaskingStrategy.HASH)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertNotNull(response.getMaskedData());
        assertNotEquals("sensitive data", response.getMaskedData());
        assertTrue(response.getMaskedData().length() > 10); // SHA-256 hash length
    }
    
    @Test
    void testMaskWithNullify() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("sensitive data")
            .piiType(PiiType.TEXT)
            .strategy(MaskingStrategy.NULLIFY)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertNull(response.getMaskedData());
    }
    
    @Test
    void testMaskWithPlaceholder() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("sensitive data")
            .piiType(PiiType.TEXT)
            .strategy(MaskingStrategy.PLACEHOLDER)
            .replacementValue("[CUSTOM_MASKED]")
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertEquals("[CUSTOM_MASKED]", response.getMaskedData());
    }
    
    @Test
    void testMaskWithPartial() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("1234567890")
            .piiType(PiiType.NUMERIC)
            .strategy(MaskingStrategy.PARTIAL)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertNotNull(response.getMaskedData());
        assertTrue(response.getMaskedData().contains("*"));
        assertNotEquals("1234567890", response.getMaskedData());
    }
    
    @Test
    void testMaskEmptyData() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("")
            .piiType(PiiType.TEXT)
            .strategy(MaskingStrategy.ASTERISK)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertEquals("", response.getMaskedData());
    }
    
    @Test
    void testMaskNullData() {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data(null)
            .piiType(PiiType.TEXT)
            .strategy(MaskingStrategy.ASTERISK)
            .build();
        
        // When
        MaskingResponse response = maskingService.maskData(request);
        
        // Then
        assertTrue(response.getSuccess());
        assertNull(response.getMaskedData());
    }
}
