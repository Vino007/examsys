package com.cnc.exam.question.test;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cnc.exam.auth.service.UserService;
import com.cnc.exam.question.entity.Question;
import com.cnc.exam.question.service.QuestionService;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:spring-mvc.xml" })
public class QuestionServiceTest {
	@Autowired
	private QuestionService questionService;
	private  ClassPathXmlApplicationContext ctx ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveTest() {
		Question question=new Question();
		question.setContent("中国现任主席是习近平吗?");
		question.setType(4);//单选
		//question.setChoices("阿里巴巴;百度;腾讯;小米;");
		question.setAnswer("true");//选第一个
		questionService.save(question);
	}
	@Test
	public void updateTest(){
		
	}
	@Test
	public void deleteTest(){
		
	}
	@Test
	public void findTest(){
		List<Question> questions=questionService.findAll();
		System.out.println(questions);
	}
}
