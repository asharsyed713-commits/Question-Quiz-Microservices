package com.example.quiz.controller;

import com.example.quiz.collections.Quiz;
import com.example.quiz.dto.QuizDto;
import com.example.quiz.service.QuizService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
@Slf4j
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(@RequestBody QuizDto quizDto){
        QuizDto quiz = quizService.create(quizDto);
        return new ResponseEntity<>(quiz, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDto> update(@PathVariable String id,@RequestBody QuizDto quizDto){
        QuizDto update = quizService.update(id, quizDto);
        return new ResponseEntity<>(update,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAll(){
//        int i=0;
//        log.info("Fetching all quizzes {})",i);
//        i++;
//        if(i<3){
//            throw new RuntimeException("Quiz service is down.Please try again later");
//        }else{
            List<QuizDto> quizzes = quizService.findAll();
            return new ResponseEntity<>(quizzes,HttpStatus.OK);
//        }

    }



    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getById(@PathVariable String id){
        QuizDto quiz = quizService.findById(id);
        return new ResponseEntity<>(quiz,HttpStatus.OK);
    }

//    public ResponseEntity<QuizDto> quizFallback(String quizId,RuntimeException e){
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        quizService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<QuizDto>> getByCat(@PathVariable Long categoryId){
        List<QuizDto> byCategory = quizService.findByCategory(categoryId);
        return new ResponseEntity<>(byCategory,HttpStatus.OK);
    }
}
