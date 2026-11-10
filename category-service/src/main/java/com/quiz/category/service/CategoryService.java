package com.quiz.category.service;

import com.quiz.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(Long categoryId,CategoryDto categoryDto);
    CategoryDto getCategory(Long categoryId);
    void deleteCategory(Long categoryId);
    List<CategoryDto> getAllCategories();

}
