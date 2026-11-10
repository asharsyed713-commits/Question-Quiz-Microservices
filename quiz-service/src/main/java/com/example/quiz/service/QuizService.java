package com.example.quiz.service;

import com.example.quiz.dto.QuizDto;

import java.util.List;

public interface QuizService {
    QuizDto create(QuizDto quizDto);
    QuizDto update(String id,QuizDto quizDto);
    void delete(String id);
    List<QuizDto> findAll();
    QuizDto findById(String id);
    List<QuizDto> findByCategory(Long categoryId);
}
