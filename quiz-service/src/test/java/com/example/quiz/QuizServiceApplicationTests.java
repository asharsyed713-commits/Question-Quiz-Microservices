package com.example.quiz;

import com.example.quiz.dto.CategoryDto;
import com.example.quiz.service.CategoryFeignService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class QuizServiceApplicationTests {

	@Autowired
	private CategoryFeignService categoryFeignService;

	@Test
	public void testFeignAllCategories(){
		System.out.println("Getting all categories");
		List<CategoryDto> all = categoryFeignService.findAll();
		all.forEach(categoryDto -> System.out.println(categoryDto.getTitle()));
		Assertions.assertEquals(3,all.size());
//		all=null;
		Assertions.assertNotNull(all);
	}


	@Test
	public void testFeignCategorybyId() {
		System.out.println("Getting category by id");
		CategoryDto categoryDto = categoryFeignService.findById(1L);
		System.out.println(categoryDto.getTitle());
		Assertions.assertNotNull(categoryDto);

	}
}
