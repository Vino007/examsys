package com.cnc.exam.exam.controller.test;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:spring-mvc.xml" })
public class ExamControllerTest {
	@Autowired
	private WebApplicationContext wac;
	EntityManagerFactory emf;//用於事務操作
	private MockMvc mockMvc;

	@Before
	public void setup() {		
		this.mockMvc = webAppContextSetup(this.wac).build();
		emf = (EntityManagerFactory) wac.getBean("entityManagerFactory");
		EntityManager em = emf.createEntityManager();
		EntityManagerHolder emHolder = new EntityManagerHolder(em);
		TransactionSynchronizationManager.bindResource(emf, emHolder);
	}

	@After
	public void clean() {
		TransactionSynchronizationManager.unbindResource(emf);
	}


	@Test
	public void search() throws Exception {

		mockMvc.perform((get("/exam/search")))
				.andExpect(status().isOk()).andDo(print());

	}
	@Test
	public void save() throws Exception {
		Date end=new Date();
		Date start=new Date(end.getTime()-1000*60*60*2);//当前时间两小时前
		
		mockMvc.perform(post("/exam/add").param("start_time",start.toString())
				.param("course.id","1" )
				.param("endTime", end.toString())
				.param("passLine", "60")
				.param("questionNumber","10")
				.param("no","00001")				
				);

	}


}
