package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "controller", "service", "adapter", "model"})
public class DemoMappingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoMappingApplication.class, args);
    }
}
