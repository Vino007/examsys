package com.cnc.exam.exam.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.common.MyPage;
import com.cnc.exam.course.service.CourseService;
import com.cnc.exam.exam.entity.Exam;
import com.cnc.exam.exam.exception.UserAlreadyHasThisExamException;
import com.cnc.exam.exam.service.ExamService;
@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController{
	@Autowired
	private ExamService examService;
	@Autowired
	private CourseService courseService;
	@ResponseBody
	//@RequiresPermissions("exam:menu")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> getALLExams(
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE + "") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType) {
		Page<Exam> examPage = examService.findAll(buildPageRequest(pageNumber));

		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", examPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}

	@ResponseBody
//	@RequiresPermissions("exam:view")
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Map<String, Object> getExamsByCondition(Exam exam,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Exam> examPage = examService.findExamByCondition(searchParams, buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", examPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	@ResponseBody
//	@RequiresPermissions("exam:view")
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public Map<String, Object> findById(Long id) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("exam", examService.findOne(id));
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;

	}
	
	@ResponseBody
//	@RequiresPermissions("exam:create")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> addExam(Exam exam) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			exam.setCourse(courseService.findOne(exam.getCourse().getId()));
			examService.save(exam);

		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "添加失败");
			e.printStackTrace();
			return resultMap;
		}
		Page<Exam> examPage = examService.findAll(buildPageRequest(1));
		data.put("page", examPage);
		resultMap.put("success", true);
		resultMap.put("data", data);
		resultMap.put("msg", "添加成功");

		return resultMap;
	}

	@ResponseBody
//	@RequiresPermissions("exam:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> deleteExams(@RequestParam("deleteIds[]") Long[] deleteIds) {
		Map<String, Object> resultMap = new HashMap<>();		
		try {
			examService.delete(deleteIds);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "删除失败");
			e.printStackTrace();
			return resultMap;
		}

		Map<String, Object> data = new HashMap<>();
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "删除成功");
		return resultMap;

	}

	@ResponseBody
//	@RequiresPermissions("exam:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> updateExam(Exam exam) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			examService.update(exam);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "修改失败");
			e.printStackTrace();
			return resultMap;
		}
		
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "修改成功");
		return resultMap;

	}
	
	@ResponseBody
//	@RequiresPermissions("exam:create")
	@RequestMapping(value = "/bindQuestion", method = RequestMethod.POST)
	public Map<String, Object> bindQuestion(Long examId,@RequestParam("questionIds[]")Long[] questionIds) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			examService.bindQuestion(examId, questionIds);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "绑定失败");
			e.printStackTrace();
			return resultMap;
		}
		
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "绑定成功");
		return resultMap;

	}
	
	@ResponseBody
//	@RequiresPermissions("exam:create")
	@RequestMapping(value = "/autoGenerate", method = RequestMethod.POST)
	public Map<String, Object> autoGenerateExam(Long examId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			examService.autoGenerateExam(examId);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "生成失败");
			e.printStackTrace();
			return resultMap;
		}
		
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "生成成功");
		return resultMap;

	}
	/**
	 * 根据状态查询某场考试的用户
	 * @param examId
	 * @param status 若为null，则查询所有用户
	 * @param pageNumber
	 * @return
	 */
	@ResponseBody
//	@RequiresPermissions("exam:view")
	@RequestMapping(value = "/findUser", method = RequestMethod.GET)
	public Map<String, Object> findUsersByStatus(Long examId,@RequestParam(required=false)Integer status,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		MyPage<User> userPage = examService.findUsersByStatus(examId,status,buildPageRequest(pageNumber));
		data.put("page", userPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "查询成功");
		return resultMap;

	}
	
	@ResponseBody
//	@RequiresPermissions("exam:view")
	@RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
	public Map<String, Object> findUsersByStatus(Long examId,@RequestParam("userIds[]")Long[] userIds,Integer status) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
			examService.updateUserStatus(examId, userIds, status);
		}catch(Exception e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "修改失败");
		}
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "修改成功");
		return resultMap;

	}
	
	@ResponseBody
//	@RequiresPermissions("exam:view")
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public Map<String, Object> addUser(Long examId,@RequestParam("userIds[]")Long[] userIds) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
			examService.addUser(examId, userIds);
		}catch(UserAlreadyHasThisExamException e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "请不要重复添加");
		}catch(Exception e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "添加失败");
		}
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "添加成功");
		return resultMap;

	}
	@ResponseBody
//	@RequiresPermissions("exam:view")
	@RequestMapping(value = "/removeUser", method = RequestMethod.POST)
	public Map<String, Object> removeUser(Long examId,@RequestParam("userIds[]")Long[] userIds) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
			examService.removeUser(examId, userIds);
		}catch(Exception e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "移除失败");
		}
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "移除成功");
		return resultMap;

	}
	/**
	 * 查找试卷问题
	 * @param id
	 * @return
	 *//*
	@ResponseBody
//	@RequiresPermissions("exam:view")
	@RequestMapping(value = "/findQuestions", method = RequestMethod.GET)
	public Map<String, Object> findQuestionById(Long id) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("exam", examService.findOne(id));
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;

	}*/

}
