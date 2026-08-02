package com.example.spring_traing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/districts")
public class Districts {
    @GetMapping
    public void getMapping(){
        System.out.println("マッピングテストです");
    }
}
