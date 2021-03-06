package com.cnc.exam.exam.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
import com.cnc.exam.auth.service.UserService;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.common.MyPage;
import com.cnc.exam.course.service.CourseService;
import com.cnc.exam.entity.json.ExamJson;
import com.cnc.exam.entity.json.UserJson;
import com.cnc.exam.exam.entity.Exam;
import com.cnc.exam.exam.exception.UserAlreadyHasThisExamException;
import com.cnc.exam.exam.exception.UserStatusErrorException;
import com.cnc.exam.exam.repository.ExamUserMidRepository;
import com.cnc.exam.exam.service.ExamService;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.log.service.LogsService;
import com.cnc.exam.question.entity.Question;
import com.cnc.exam.result.entity.ExamResultEntity;
@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController{
	@Autowired
	private ExamService examService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private LogsService logsService;
	@ResponseBody
	@RequiresPermissions("exam:menu")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> getALLExams(
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE + "") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		int flag= 0 ;
		Subject curUser=SecurityUtils.getSubject();
		Session session=curUser.getSession();	
		User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
		if(null==currentUser){
			return null;
		}
		Set<String> allPermission = userService.findAllPermissionsByUsername(currentUser.getUsername());
		
		for(String item : allPermission){
			if("exam:viewAll".equals(item)){
				flag=1;
				Page<Exam> 	examPage=examService.findAll(buildPageRequest(pageNumber));
				data.put("page", examPage);
				break;						
			}
					
		}
		if(flag==0){
			MyPage<Exam> examPage=examService.findExamByUser2(currentUser.getId(),buildPageRequest(pageNumber));
			
			data.put("page", examPage);
		}
									
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}

	@ResponseBody
	@RequiresPermissions("exam:viewOwn")
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Map<String, Object> getExamsByCondition(Exam exam,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		int flag= 0 ;
		Subject curUser=SecurityUtils.getSubject();
		Session session=curUser.getSession();	
		User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
		if(null==currentUser){
			return null;
		}
		
		Set<String> allPermission = userService.findAllPermissionsByUsername(currentUser.getUsername());
		
		for(String item : allPermission){
			if("exam:viewAll".equals(item)){
				flag=1;
				Page<Exam> examPage = examService.findExamByCondition(searchParams, buildPageRequest(pageNumber));
				data.put("page", examPage);
				break;						
			}
					
		}
		if(flag==0){
			MyPage<ExamJson> examPage=examService.findExamByUser(currentUser.getId(),buildPageRequest(pageNumber));
			data.put("page", examPage);
		}
										
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	@ResponseBody
	@RequiresPermissions("exam:viewOwn")
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
	@RequiresPermissions("exam:create")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> addExam(Exam exam,Long courseId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();

		
		try {
			if(courseId!=null&&courseService.findOne(courseId)!=null){
				exam.setCourse(courseService.findOne(courseId));
			}			
			examService.save(exam);
			resultMap.put("success", true);
			resultMap.put("data", data);
			resultMap.put("msg", "添加成功");
			logsService.save(new LogsEntity(getCurrentUser(), 2, "添加一场考试",new Timestamp(new Date().getTime())));

		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "添加失败");
			e.printStackTrace();
		}


		return resultMap;
	}

	@ResponseBody
	@RequiresPermissions("exam:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> deleteExams(@RequestParam("deleteIds[]") Long[] deleteIds) {
		Map<String, Object> resultMap = new HashMap<>();		
		Map<String, Object> data = new HashMap<>();
		try {
			examService.clearConnection(deleteIds);
			examService.delete(deleteIds);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "删除成功");
			logsService.save(new LogsEntity(getCurrentUser(), 2, "删除考试",new Timestamp(new Date().getTime())));
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("data", data);
			resultMap.put("msg", "删除失败");
			logsService.save(new LogsEntity(getCurrentUser(), 2, "删除考试",new Timestamp(new Date().getTime())));
			e.printStackTrace();
		}

	

		return resultMap;

	}
	/**
	 * 当courseId不存在的时候会更新失败
	 * @param exam
	 * @param courseId
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("exam:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> updateExam(Exam exam,Long courseId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		if(courseId!=null){
			exam.setCourse(courseService.findOne(courseId));
		}
		try {
			examService.update(exam);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "修改成功");
			logsService.save(new LogsEntity(getCurrentUser(), 2, "更新考试",new Timestamp(new Date().getTime())));
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "修改失败");
			e.printStackTrace();
		}
		
		return resultMap;

	}
	
	@ResponseBody
	@RequiresPermissions("exam:bindQuestion")
	@RequestMapping(value = "/prepareBindQuestion", method = RequestMethod.GET)
	public Map<String, Object> prepartBindQuestion(Long examId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Exam exam=examService.findOne(examId);
			List<Long> selectIds=new ArrayList<>();
			List<Question> selectQuestions=exam.getQuestions();
			for(Question q:selectQuestions){
				selectIds.add(q.getId());
			}
			List<Question> allQuestion=exam.getCourse().getQuestions();
			for(Question q:selectQuestions){
				allQuestion.remove(q);
			}
			data.put("selectIds", selectIds);
			data.put("selectQuestions", selectQuestions);
			data.put("noSelectQuestions", allQuestion);
			
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "查询成功");
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "查询失败");
			e.printStackTrace();
		}
		

		return resultMap;

	}
	
	@ResponseBody
	@RequiresPermissions("exam:bindMockQuestion")
	@RequestMapping(value = "/prepareBindMockQuestion", method = RequestMethod.GET)
	public Map<String, Object> prepartBindMockQuestion(Long examId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			Exam exam=examService.findOne(examId);
			List<Long> selectIds=new ArrayList<>();
			List<Question> selectQuestions=exam.getMockQuestions();
			for(Question q:selectQuestions){
				selectIds.add(q.getId());
			}
			List<Question> allQuestion=exam.getCourse().getQuestions();
			for(Question q:selectQuestions){
				allQuestion.remove(q);
			}
			data.put("selectIds", selectIds);
			data.put("selectQuestions", selectQuestions);
			data.put("noSelectQuestions", allQuestion);
			
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "查询成功");
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "查询失败");
			e.printStackTrace();
		}
		

		return resultMap;

	}
	
	
	@ResponseBody
	@RequiresPermissions("exam:bindQuestion")
	@RequestMapping(value = "/bindQuestion", method = RequestMethod.POST)
	public Map<String, Object> bindQuestion(Long examId,@RequestParam("questionIds[]")Long[] questionIds) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			examService.bindQuestion(examId, questionIds);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "绑定成功");
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "绑定失败");
			e.printStackTrace();
		}
		

		return resultMap;

	}
	
	@ResponseBody
	@RequiresPermissions("exam:bindMockQuestion")
	@RequestMapping(value = "/bindMockQuestion", method = RequestMethod.POST)
	public Map<String, Object> bindMockQuestion(Long examId,@RequestParam("questionIds[]")Long[] questionIds) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			examService.bindMockQuestion(examId, questionIds);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "绑定成功");
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "绑定失败");
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequiresPermissions("exam:autoGenerate")
	@RequestMapping(value = "/autoGenerate", method = RequestMethod.POST)
	public Map<String, Object> autoGenerateExam(Long examId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		try {
			examService.autoGenerateExam(examId);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "生成成功");
			logsService.save(new LogsEntity(getCurrentUser(),2, "生成试卷",new Timestamp(new Date().getTime())));
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "生成失败");
			e.printStackTrace();
		}
		

		return resultMap;

	}
	
	@ResponseBody
	@RequiresPermissions("exam:autoGenerateMock")
	@RequestMapping(value = "/autoGenerateMock", method = RequestMethod.POST)
	public Map<String, Object> autoGenerateMockExam(Long examId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();		
		try {
			examService.autoGenerateMockExam(examId);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "生成成功");
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "生成失败");
			e.printStackTrace();
		}
		

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
	@RequiresPermissions("exam:viewAll")
	@RequestMapping(value = "/findUser", method = RequestMethod.GET)
	public Map<String, Object> findUsersByStatus(Long examId,@RequestParam(required=false)Integer status,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		MyPage<UserJson> userPage = examService.findUsersByStatus(examId,status,buildPageRequest(pageNumber));
		data.put("page", userPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "查询成功");
		return resultMap;

	}
	
	@ResponseBody
	@RequiresPermissions("exam:update")
	@RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
	public Map<String, Object> findUsersByStatus(Long examId,@RequestParam("userIds[]")Long[] userIds,Integer status) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
			examService.updateUserStatus(examId, userIds, status);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "修改成功");
			logsService.save(new LogsEntity(getCurrentUser(), 2, "修改参考人员状态",new Timestamp(new Date().getTime())));
		}catch(Exception e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "修改失败");
			e.printStackTrace();
		}

		return resultMap;

	}
	
	@ResponseBody
	@RequiresPermissions("exam:update")
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public Map<String, Object> addUser(Long examId,@RequestParam("userIds[]")Long[] userIds) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
			examService.addUser(examId, userIds);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "添加成功");
			logsService.save(new LogsEntity(getCurrentUser(), 2, "添加参考人员",new Timestamp(new Date().getTime())));
		}catch(UserAlreadyHasThisExamException e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "请不要重复添加");
			e.printStackTrace();
		}catch(Exception e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "添加失败");
			e.printStackTrace();
		}

		return resultMap;

	}
	@ResponseBody
	@RequiresPermissions("exam:update")
	@RequestMapping(value = "/removeUser", method = RequestMethod.POST)
	public Map<String, Object> removeUser(Long examId,@RequestParam("userIds[]")Long[] userIds) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
			examService.removeUser(examId, userIds);
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "移除成功");
		}catch(Exception e){
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "移除失败");
			e.printStackTrace();
			
		}

		return resultMap;

	}
	/**
	 * 查找正常试卷问题
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("exam:join")
	@RequestMapping(value = "/findQuestions", method = RequestMethod.GET)
	public Map<String, Object> findQuestionstionById(Long examId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		Exam exam=examService.findOne(examId);
		List<Question> questions=exam.getQuestions();
		List<QuestionJson> questionJsons=new ArrayList<>();
		for(Question q:questions){
			QuestionJson questionJson=new QuestionJson();
			questionJson.setContent(q.getContent());
			questionJson.setChoices(q.getChoices());
			questionJson.setType(q.getType());
			questionJson.setContentImgageUrl(q.getContentImageUrl());
			questionJson.setId(q.getId());
			questionJsons.add(questionJson);
		}
		
		data.put("questions",questionJsons);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;

	}
	
	/**
	 * 查找模拟试卷问题
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("exam:join")
	@RequestMapping(value = "/findMockQuestions", method = RequestMethod.GET)
	public Map<String, Object> findMockQuestionstionById(Long examId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		Exam exam=examService.findOne(examId);
		List<Question> questions=exam.getMockQuestions();
		List<QuestionJson> questionJsons=new ArrayList<>();
		for(Question q:questions){
			QuestionJson questionJson=new QuestionJson();
			questionJson.setContent(q.getContent());
			questionJson.setChoices(q.getChoices());
			questionJson.setType(q.getType());
			questionJson.setContentImgageUrl(q.getContentImageUrl());
			questionJson.setId(q.getId());
			questionJsons.add(questionJson);
		}
		
		data.put("questions",questionJsons);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;

	}
	
	@ResponseBody
	@RequiresPermissions("exam:join")
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public Map<String, Object> submitExam(Long userId,Long examId,boolean isMock,@RequestParam("performances[]")String[] performances) {
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		//判题
		try{
			ExamResultEntity examResult=examService.judgeExam(userId, examId, performances,isMock);
			String answerIsRight=examResult.getAnswerIsRight();
			String[] answerResult=answerIsRight.split(";");

			String[] performanceArray=examResult.getPerformance().split("\\$;");
			
			data.put("performances", performanceArray);
			data.put("answerIsRight", answerResult);
			data.put("score", examResult.getScore());
			data.put("isPass", examResult.getIsPass());//1表示通过
			resultMap.put("data", data);
			resultMap.put("success", true);
			resultMap.put("msg", "提交成功");
			logsService.save(new LogsEntity(getCurrentUser(), 2, "完成一场考试",new Timestamp(new Date().getTime())));
		}catch(UserStatusErrorException e){
			e.printStackTrace();
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "用户已经参加过考试或已请假");
			
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("data", data);
			resultMap.put("success", false);
			resultMap.put("msg", "提交失败");
		}
		
		return resultMap;

	}
	
	/**
	 * 用户查询自己需要参加的考试
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("exam:viewOwn")
	@RequestMapping(value = "/findExamByUser", method = RequestMethod.GET)
	public Map<String, Object> findExamByUser(Long userId,@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		MyPage<ExamJson> examPage = examService.findExamByUser(userId,buildPageRequest(pageNumber));
		data.put("page",examPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;

	}
	
	private class QuestionJson{
		private long id;
		private String content;
		/**
		 * 题干图片url
		 */
		private String contentImgageUrl;
		/**
		 * 选项
		 */
		private String choices;
		
		/**
		 * 题目类型 1.单选 2.多选 3.填空 4.判断
		 */
		
		private Integer type;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getContentImgageUrl() {
			return contentImgageUrl;
		}

		public void setContentImgageUrl(String contentImgageUrl) {
			this.contentImgageUrl = contentImgageUrl;
		}

		public String getChoices() {
			return choices;
		}

		public void setChoices(String choices) {
			this.choices = choices;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
		
		
	}

}
