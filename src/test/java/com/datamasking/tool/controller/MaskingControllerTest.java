package com.datamasking.tool.controller;

import com.datamasking.tool.dto.MaskingRequest;
import com.datamasking.tool.dto.MaskingResponse;
import com.datamasking.tool.model.MaskingRule;
import com.datamasking.tool.model.MaskingStrategy;
import com.datamasking.tool.model.PiiType;
import com.datamasking.tool.service.MaskingService;
import com.datamasking.tool.repository.MaskingRuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for MaskingController
 */
@WebMvcTest(MaskingController.class)
class MaskingControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private MaskingService maskingService;
    
    @MockBean
    private MaskingRuleRepository maskingRuleRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @WithMockUser
    void testMaskData() throws Exception {
        // Given
        MaskingRequest request = MaskingRequest.builder()
            .data("john.doe@example.com")
            .piiType(PiiType.EMAIL)
            .strategy(MaskingStrategy.ASTERISK)
            .build();
        
        MaskingResponse response = MaskingResponse.builder()
            .originalData("john.doe@example.com")
            .maskedData("j***e@example.com")
            .piiType("EMAIL")
            .strategy("ASTERISK")
            .success(true)
            .build();
        
        when(maskingService.maskData(any(MaskingRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/masking/mask")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.originalData").value("john.doe@example.com"))
                .andExpect(jsonPath("$.maskedData").value("j***e@example.com"));
    }
    
    @Test
    void testGetPiiTypes() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/masking/pii-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("EMAIL"));
    }
    
    @Test
    void testGetMaskingStrategies() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/masking/strategies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("ASTERISK"));
    }
    
    @Test
    @WithMockUser
    void testGetMaskingRules() throws Exception {
        // Given
        MaskingRule rule = new MaskingRule();
        rule.setId(1L);
        rule.setPiiType(PiiType.EMAIL);
        rule.setStrategy(MaskingStrategy.ASTERISK);
        rule.setIsActive(true);
        
        when(maskingRuleRepository.findAll()).thenReturn(List.of(rule));
        
        // When & Then
        mockMvc.perform(get("/api/v1/masking/rules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].piiType").value("EMAIL"));
    }
    
    @Test
    @WithMockUser
    void testGetMaskingRuleByPiiType() throws Exception {
        // Given
        MaskingRule rule = new MaskingRule();
        rule.setId(1L);
        rule.setPiiType(PiiType.EMAIL);
        rule.setStrategy(MaskingStrategy.ASTERISK);
        rule.setIsActive(true);
        
        when(maskingRuleRepository.findByPiiType(PiiType.EMAIL)).thenReturn(Optional.of(rule));
        
        // When & Then
        mockMvc.perform(get("/api/v1/masking/rules/EMAIL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.piiType").value("EMAIL"))
                .andExpect(jsonPath("$.strategy").value("ASTERISK"));
    }
    
    @Test
    @WithMockUser
    void testCreateMaskingRule() throws Exception {
        // Given
        MaskingRule rule = new MaskingRule();
        rule.setPiiType(PiiType.EMAIL);
        rule.setStrategy(MaskingStrategy.ASTERISK);
        rule.setIsActive(true);
        
        MaskingRule savedRule = new MaskingRule();
        savedRule.setId(1L);
        savedRule.setPiiType(PiiType.EMAIL);
        savedRule.setStrategy(MaskingStrategy.ASTERISK);
        savedRule.setIsActive(true);
        
        when(maskingRuleRepository.save(any(MaskingRule.class))).thenReturn(savedRule);
        
        // When & Then
        mockMvc.perform(post("/api/v1/masking/rules")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.piiType").value("EMAIL"));
    }
    
    @Test
    void testHealthCheck() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/masking/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Data Masking Tool"));
    }
}
