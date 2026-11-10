package com.example.quiz.service;

import com.example.quiz.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="CATEGORY-SERVICE")
public interface CategoryFeignService {

    @GetMapping("api/v1/categories")
    List<CategoryDto> findAll();

    @GetMapping("api/v1/categories/{id}")
    CategoryDto findById(@PathVariable Long id);

    @PostMapping("api/v1/categories")
    CategoryDto createCategory(@RequestBody CategoryDto categoryDto);

    @PutMapping("api/v1/categories/{id}")
    CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto);

    @DeleteMapping("api/v1/categories/{id}")
    void deleteCategory(@PathVariable Long id);

}
