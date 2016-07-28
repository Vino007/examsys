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
		resultMap.put("success", true);
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
		resultMap.put("success", true);
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
		resultMap.put("success", true);
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
			resultMap.put("success", false);
			resultMap.put("msg", "条件失败");
			e.printStackTrace();
			return resultMap;
		}
		Page<Question> questionPage = questionService.findAll(buildPageRequest(1));
		data.put("page", questionPage);
		resultMap.put("success", true);
		resultMap.put("data", data);
		resultMap.put("msg", "添加成功");

		return resultMap;
	}

	@ResponseBody
//	@RequiresPermissions("question:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> deleteQuestions(@RequestParam("deleteIds[]") Long[] deleteIds) {
		Map<String, Object> resultMap = new HashMap<>();
		
		List<Question> questions=questionService.find(deleteIds);
		for(Question q:questions){
			if(q.getIsExam()){
				resultMap.put("success", false);
				resultMap.put("msg", "不能删除已经考过的题目，请重新选择");
				return resultMap;
			}
		}
		try {
			questionService.delete(deleteIds);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "删除失败");
			e.printStackTrace();
			return resultMap;
		}

		Page<Question> questionPage = questionService.findAll(buildPageRequest(1));

		Map<String, Object> data = new HashMap<>();
		data.put("page", questionPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "删除成功");
		return resultMap;

	}

	@ResponseBody
//	@RequiresPermissions("question:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> updateQuestion(Question question) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		Question question2=questionService.findOne(question.getId());
		
		Boolean isExam=question2.getIsExam();
		if(isExam){
			resultMap.put("success", false);
			resultMap.put("msg", "修改失败,该题目已经被考过，不能修改");
			return resultMap;
		}
		try {
			questionService.update(question);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "修改失败");
			e.printStackTrace();
			return resultMap;
		}
		
		Page<Question> questionPage = questionService.findAll(buildPageRequest(1));		
		data.put("page", questionPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "修改成功");
		return resultMap;

	}
	/**
	 * 下线题目
	 * @param id question的主键id
	 * @return
	 */
	@ResponseBody
//	@RequiresPermissions("question:update")
	@RequestMapping(value = "/offline", method = RequestMethod.POST)
	public Map<String, Object> offlineQuestion(Long id) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			questionService.offlineQuestion(id);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "题目不存在,下线失败");
			e.printStackTrace();
			return resultMap;
		}
		
		//Page<Question> questionPage = questionService.findAll(buildPageRequest(1));		
		//data.put("page", questionPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "下线成功");
		return resultMap;

	}
	/**
	 * 上线题目
	 * @param id question的主键id
	 * @return
	 */
	@ResponseBody
//	@RequiresPermissions("question:update")
	@RequestMapping(value = "/online", method = RequestMethod.POST)
	public Map<String, Object> onlineQuestion(Long id) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			questionService.onlineQuestion(id);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "题目不存在,上线失败");
			e.printStackTrace();
			return resultMap;
		}
		
		/*Page<Question> questionPage = questionService.findAll(buildPageRequest(1));		
		data.put("page", questionPage);*/
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "上线成功");
		return resultMap;

	}
	
	
}
