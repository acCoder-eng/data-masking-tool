package com.datamasking.tool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web Controller for serving HTML pages
 */
@Controller
public class WebController {
    
    /**
     * Database configuration page
     */
    @GetMapping("/database-config")
    public String databaseConfig() {
        return "database-config";
    }
    
    /**
     * Home page
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/database-config";
    }
}
