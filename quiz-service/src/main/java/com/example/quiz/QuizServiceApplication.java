package com.example.quiz;

import com.example.quiz.collections.Quiz;
import com.example.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class QuizServiceApplication {

//	@Autowired
//	private QuizRepository quizRepository;
//
//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	@Autowired
//	private org.springframework.core.env.Environment env;


	public static void main(String[] args) {
		SpringApplication.run(QuizServiceApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		Quiz quiz = Quiz.builder()
//				.id(UUID.randomUUID().toString())
//				.title("Java")
//				.description("This is java quiz")
//				.maxMarks(100)
//				.timeLimit(30)
//				.createdBy("Ashar")
//				.noOfQuestions(10)
//				.imageUrl("https://unsplash.com/photos/two-people-on-cliff-overlooking-ocean-QtGd-14_eHI")
//				.live(true)
//				.passingMarks(30)
//				.build();
//
//		Quiz savedQuiz = quizRepository.save(quiz);
//		System.out.println("Quiz saved with id: "+savedQuiz.getId());
//		System.out.println("MongoDB database: " +
//				mongoTemplate.getDb().getName());
//
//		System.out.println(
//				"Mongo URI = " +
//						env.getProperty("spring.data.mongodb.uri")
//		);
//
//		System.out.println(
//				"Mongo database property = " +
//						env.getProperty("spring.data.mongodb.database")
//		);

//		System.out.println(
//				"MongoTemplate database = " +
//						mongoTemplate.getDb().getName()
//		);
//
//	}
}
