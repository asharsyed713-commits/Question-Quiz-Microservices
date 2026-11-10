    package com.example.quiz.dto;

    import lombok.*;
    import lombok.extern.slf4j.Slf4j;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Slf4j
    public class CategoryDto {

        private Long id;

        private String title;

        private String description;

        private boolean active;
    }
