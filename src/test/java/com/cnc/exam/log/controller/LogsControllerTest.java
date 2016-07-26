package com.cnc.exam.log.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:spring-mvc.xml" })
public class LogsControllerTest {

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
		mockMvc.perform((get("/logs/search?search_opType=1")))
				.andExpect(status().isOk()).andDo(print());

	}
}
