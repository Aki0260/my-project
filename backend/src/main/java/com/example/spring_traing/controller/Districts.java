package com.example.spring_traing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Districts controller for managing district information.
 */
@RestController
@RequestMapping("/districts")
public class Districts {
    
    /**
     * Get districts mapping.
     *
     * @return message string
     */
    @GetMapping
    public String getMapping() {
        System.out.println("マッピングテストです");
        return "マッピングテストです";
    }
}