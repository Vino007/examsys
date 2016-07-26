package com.cnc.exam.log.service;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cnc.exam.log.entity.LogsEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:spring-mvc.xml" })
public class LogsServiceTest {

	@Autowired
	private LogsService logsService;
	@Test
	public void test() {
		LogsEntity logsEntity = new LogsEntity(1, 1+"", 1, 1+"", new Timestamp(new Date().getTime()));
		logsService.save(logsEntity);
	}

}
