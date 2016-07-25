package com.vino.exam.auth.test;

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
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:spring-mvc.xml" })
public class UserServiceTest {
	@Autowired
	private UserService userService;
	private  ClassPathXmlApplicationContext ctx ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Before
	public void setUp() throws Exception {
		// ctx =  new ClassPathXmlApplicationContext("applicationContextTest.xml"); 
		// userService=ctx.getBean("userService",UserService.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		long count=userService.getCount();
		System.out.println(count);
	}
}
