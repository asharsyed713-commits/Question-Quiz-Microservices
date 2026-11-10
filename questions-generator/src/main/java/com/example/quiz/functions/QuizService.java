package com.example.quiz.functions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class QuizService {

    @Bean(name="getQuizBinding")
    public Function<QuizDto,String> getQuizBinding(){
        return quizDto ->{
            System.out.println("Quiz Created event received");
            System.out.println(quizDto.getTitle());
            return "Quiz created successfully";
        };
    }
}
