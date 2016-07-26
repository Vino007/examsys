package com.cnc.exam.question.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.question.entity.Question;
import com.cnc.exam.question.service.QuestionService;

@Controller
@RequestMapping("/question")
public class QuestionController extends BaseController{
	@Autowired
	private QuestionService questionService;
	
	@ResponseBody
	//@RequiresPermissions("question:menu")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> getALLQuestions(
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE + "") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType) {
		Page<Question> questionPage = questionService.findAll(buildPageRequest(pageNumber));

		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", questionPage);
		resultMap.put("data", data);
		resultMap.put("successs", true);
		return resultMap;
	}

	@ResponseBody
//	@RequiresPermissions("question:view")
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Map<String, Object> getQuestionsByCondition(Question question,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Question> questionPage = questionService.findQuestionByCondition(searchParams, buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", questionPage);
		resultMap.put("data", data);
		resultMap.put("successs", true);
		return resultMap;
	}
	@ResponseBody
//	@RequiresPermissions("question:view")
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public Map<String, Object> findById(Long id) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("question", questionService.findOne(id));
		resultMap.put("data", data);
		resultMap.put("successs", true);
		return resultMap;

	}
	
	@ResponseBody
//	@RequiresPermissions("question:create")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> addQuestion(Question question) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			questionService.save(question);

		} catch (Exception e) {
			resultMap.put("successs", false);
			resultMap.put("msg", "条件失败");
			e.printStackTrace();
			return resultMap;
		}
		Page<Question> questionPage = questionService.findAll(buildPageRequest(1));
		data.put("page", questionPage);
		resultMap.put("successs", true);
		resultMap.put("data", data);
		resultMap.put("msg", "添加成功");

		return resultMap;
	}

	@ResponseBody
//	@RequiresPermissions("question:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> deleteQuestions(@RequestParam("deleteIds[]") Long[] deleteIds) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			questionService.delete(deleteIds);
		} catch (Exception e) {
			resultMap.put("successs", false);
			resultMap.put("msg", "删除失败");
			e.printStackTrace();
			return resultMap;
		}

		Page<Question> questionPage = questionService.findAll(buildPageRequest(1));

		Map<String, Object> data = new HashMap<>();
		data.put("page", questionPage);
		resultMap.put("data", data);
		resultMap.put("successs", true);
		resultMap.put("msg", "删除成功");
		return resultMap;

	}

	@ResponseBody
//	@RequiresPermissions("question:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> updateQuestion(Question question) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			questionService.update(question);// 密码这里要做加密处理
		} catch (Exception e) {
			resultMap.put("successs", false);
			resultMap.put("msg", "更新失败");
			e.printStackTrace();
			return resultMap;
		}
		
		Page<Question> questionPage = questionService.findAll(buildPageRequest(1));		
		data.put("page", questionPage);
		resultMap.put("data", data);
		resultMap.put("successs", true);
		resultMap.put("msg", "更新成功");
		return resultMap;

	}
	
}
