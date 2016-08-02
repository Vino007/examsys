package com.cnc.exam.result.service;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cnc.exam.log.utils.FastJsonTool;
import com.cnc.exam.result.entity.ExamSituation;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:spring-mvc.xml" })
public class ExamResultServiceTest {

	@Autowired
	private ExamResultService examResultService;
	@Test
	public void test() {
		Map<String, ExamSituation> map = examResultService.getConditionEntities(1);
		System.out.println(FastJsonTool.createJsonString(map));
	}

}
