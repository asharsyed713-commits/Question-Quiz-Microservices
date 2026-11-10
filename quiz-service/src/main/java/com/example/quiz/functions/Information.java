package com.example.quiz.functions;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class Information {

    @Bean
    public Supplier<String> getInformation(){
        return () -> "Hello World";
    }

    @Bean
    public Function<String,String> consumeInformation(){
        return String::toUpperCase;
    }

    @Bean
    public Consumer<String> acknowledgeInformation(){
        return info -> System.out.println("Acknowledgement received: " + info);
    }
}
