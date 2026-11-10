package com.example.quiz.service;

import com.example.quiz.collections.Quiz;
import com.example.quiz.dto.CategoryDto;
import com.example.quiz.dto.QuizDto;
import com.example.quiz.repository.QuizRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class QuizServiceImpl implements QuizService{

    private final QuizRepository repository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final WebClient.Builder webClientBuilder;
    private final CategoryFeignService categoryFeignService;
    private  StreamBridge streamBridge;

    public QuizServiceImpl(QuizRepository repository, ModelMapper modelMapper, RestTemplate restTemplate, WebClient.Builder webClientBuilder, CategoryFeignService categoryFeignService,StreamBridge streamBridge) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.webClient = webClientBuilder.baseUrl("http://CATEGORY-SERVICE").build();
        this.webClientBuilder = webClientBuilder;
        this.categoryFeignService = categoryFeignService;
        this.streamBridge = streamBridge;
    }


    @Override
    public QuizDto create(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        modelMapper.map(quizDto, quiz);
        quiz.setCategoryId(quizDto.getCategoryId());
        quiz.setId(UUID.randomUUID().toString());
        String url = "http://CATEGORY-SERVICE/api/v1/categories/" + quizDto.getCategoryId();
        log.info(url);
        CategoryDto category = restTemplate.getForObject(url, CategoryDto.class);

        Quiz savedQuiz = repository.save(quiz);
        QuizDto response = modelMapper.map(savedQuiz, QuizDto.class);
        response.setCategory(category);

        publishQuizCreatedEvent(response);
        return response;
    }

    //Event publish
    private void publishQuizCreatedEvent(QuizDto quizDto) {

        log.info("Quiz created. Publishing event to RabbitMQ");

        boolean success = streamBridge.send(
                "quizCreatedBinding-out-0",
                quizDto
        );

        if (success) {
            log.info("Quiz event successfully sent to RabbitMQ");
        } else {
            log.error("Failed to publish quiz event to RabbitMQ");
            throw new RuntimeException("Quiz event publishing failed");
        }
    }

    @Override
    public QuizDto update(String id, QuizDto quizDto) {
        Quiz quiz = repository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setMaxMarks(quizDto.getMaxMarks());
        quiz.setTimeLimit(quizDto.getTimeLimit());
        quiz.setCreatedBy(quizDto.getCreatedBy());
        quiz.setNoOfQuestions(quizDto.getNoOfQuestions());
        quiz.setImageUrl(quizDto.getImageUrl());
        quiz.setLive(quizDto.isLive());
        quiz.setPassingMarks(quizDto.getPassingMarks());
        quiz.setCategoryId(quizDto.getCategoryId());
        Quiz savedQuiz = repository.save(quiz);
        return modelMapper.map(savedQuiz, QuizDto.class);
    }

    @Override
    public void delete(String id) {
        Quiz quiz = repository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        repository.delete(quiz);
    }

    @Override
    public List<QuizDto> findAll() {
        List<Quiz> quizzes = repository.findAll();
//        return quizzes.stream().map(q -> modelMapper.map(q, QuizDto.class)).collect(Collectors.toList());
        List<QuizDto> list = quizzes.stream().map(quiz -> {

            Long categoryId = quiz.getCategoryId();
            QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
            try {
                CategoryDto category = this.webClient.get()
                        .uri("/api/v1/categories/{categoryId}", categoryId)
                        .retrieve()
                        .bodyToMono(CategoryDto.class)
                        .block();


                quizDto.setCategory(category);

            }catch(WebClientResponseException ex){
                log.error("Category not found");
                quizDto.setCategory(null );
                ex.printStackTrace();
            }
            return quizDto;

        }).toList();

        return list;
    }

    @Override
    @CircuitBreaker(name = "quizCB", fallbackMethod = "quizFallback")
    public QuizDto findById(String id) {
        Quiz quiz = repository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
        QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
        Long categoryId = quiz.getCategoryId();
//        String url = "http://localhost:9091/api/v1/categories/" + categoryId;
        String url = "http://CATEGORY-SERVICE/api/v1/categories/" + categoryId;
        log.info(url);
        log.info("Calling Category Service");
        CategoryDto category = restTemplate.getForObject(url, CategoryDto.class);
        log.info("Category Service response received");
        quizDto.setCategory(category);
        return quizDto;
    }

    //fallback method for findById
    public QuizDto quizFallback(String id,Throwable t) {
        log.error("Category not found");
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setTitle("Falhttpack Category");
        return new QuizDto();
    }

    @Override
    public List<QuizDto> findByCategory(Long categoryId) {
        List<Quiz> quizzes = repository.findByCategoryId(categoryId);
       return quizzes.stream().map(q->{
           QuizDto quizDto = modelMapper.map(q, QuizDto.class);
           CategoryDto categoryDto = null;
           try {
               categoryDto = categoryFeignService.findById(quizDto.getCategoryId());
           } catch (FeignException.NotFound ex) {
               log.error("Category not found");
           }
           quizDto.setCategory(categoryDto);
           return quizDto;
       }).toList();
    }
}
