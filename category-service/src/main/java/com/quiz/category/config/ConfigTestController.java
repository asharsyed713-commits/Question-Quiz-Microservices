package com.quiz.category.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/config")
@RefreshScope
public class ConfigTestController {

    @Value("${config.value}")
    private String configValue;

    @GetMapping
    public String getConfig() {
        System.out.println("Config value is: "+configValue);
        return configValue;
    }
}
