package com.cnc.exam.result.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.service.UserService;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.log.service.LogsService;
import com.cnc.exam.log.utils.FastJsonTool;
import com.cnc.exam.result.entity.ExamResultEntity;
import com.cnc.exam.result.entity.ExamSituation;
import com.cnc.exam.result.service.ExamResultService;

@Controller
@RequestMapping("/examresult")
public class ExamResultController extends BaseController {

	@Autowired
	private ExamResultService examResultService;
	
	@Autowired
	private LogsService logsService;
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequiresPermissions("examResult:create")
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public Map<String, Object> insertER(Model model, ExamResultEntity examResultEntity){
		Map<String, Object> resultMap = new HashMap<>();
		try{
			examResultEntity.setCreateTime(new Date());
			examResultService.save(examResultEntity);
			resultMap.put("data", "");
			resultMap.put("success", true);
			resultMap.put("msg", "添加成功");
		}catch(Exception e){
			resultMap.put("data", "");
			resultMap.put("success", false);
			resultMap.put("msg", "添加失败");
		}
		return resultMap;
	}
	
	/**
	 * 根据条件搜索
	 * @param model
	 * @param user
	 * @param pageNumber
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Map<String, Object> getLogsByCondition(Model model, ExamResultEntity examResultEntity,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		int flag= 0 ;
		Subject curUser=SecurityUtils.getSubject();
		Session session=curUser.getSession();	
		User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
		if(null==currentUser){
			return null;
		}
		Set<String> allPermission = userService.findAllPermissionsByUsername(currentUser.getUsername());
		
		for(String item : allPermission){
			if("examResult:viewAll".equals(item)){
				flag = 1;
			}
		}
		if(flag == 1){
			
		}else{
			String userName = (String) searchParams.get("username");
			if(searchParams.containsKey("username")){
				searchParams.remove("username");
			}
			searchParams.put("username", currentUser.getUsername());
		}
		
		Page<ExamResultEntity> logsPage = examResultService.findERByCondition(searchParams, buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", logsPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg","查询成功");
		return resultMap;
	}
	
	/**
	 * 根据id删除
	 * @param model
	 * @param deleteIds
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("examResult:delete")
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public  Map<String, Object> deleteER(Model model,@RequestParam("deleteIds[]")Long[] deleteIds){
		Map<String, Object> resultMap = new HashMap<>();
		try{
			examResultService.deleteEntity(deleteIds);
		}catch(Exception e){
			resultMap.put("msg", "删除失败");
			resultMap.put("success", false);
		}
		resultMap.put("data", "");
		resultMap.put("msg", "删除成功");
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 更新成绩
	 * @param model
	 * @param er
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("examResult:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)	
	public Map<String, Object> updateExamResult(Model model,ExamResultEntity er){
		Subject curUser=SecurityUtils.getSubject();
		Session session=curUser.getSession();	
		User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
		if(null==currentUser){
			return null;
		}
		currentUser = userService.findOne(currentUser.getId());
		LogsEntity logsEntity = new LogsEntity(currentUser,2,"更新了考试成绩",new Timestamp(new Date().getTime()));
		
		logsService.save(logsEntity);
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		examResultService.update(er);
		data.put("page", "");
		resultMap.put("data", data);
		resultMap.put("msg", "更新成功");
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 导出成绩
	 * @param model
	 * @param er
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequiresPermissions("examResult:export")
	@RequestMapping(value="/download ",method=RequestMethod.GET)	
	public ResponseEntity<byte[]> downloadEaxmResult(Model model,HttpSession session) throws IOException{
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		String realPath=session.getServletContext().getRealPath("/WEB-INF/upload");
		String fileName="examresult"+System.currentTimeMillis()+".xls";
		examResultService.saveToExcel(realPath+"\\"+fileName);
		HttpHeaders headers = new HttpHeaders();    
		headers.setContentDispositionFormData("attachment", fileName); 	
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(realPath+"\\"+fileName)),    
				                                  headers, HttpStatus.CREATED);
		/*try {
			
			data.put("page", "");
			resultMap.put("data", data);
			resultMap.put("msg", "下载成功");
			resultMap.put("success", true);
		} catch (FileNotFoundException e) {
			data.put("page", "");
			resultMap.put("data", data);
			resultMap.put("msg", "下载失败");
			resultMap.put("success", false);
			e.printStackTrace();
		}
		
		return resultMap;*/
	}
	
	/**
	 * 导出成绩
	 * @param model
	 * @param er
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequiresPermissions("examResult:export")
	@RequestMapping(value="/downloadspecial ",method=RequestMethod.GET)	
	public ResponseEntity<byte[]> downloadSpecialEaxmResult(Model model,HttpSession session,@RequestParam("ids[]")

String[] ids) throws IOException{
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		String realPath=session.getServletContext().getRealPath("/WEB-INF/upload");
		String fileName="userExport"+System.currentTimeMillis()+".xls";
		examResultService.saveToExcel(realPath+"\\"+fileName,ids);
		HttpHeaders headers = new HttpHeaders();    
		headers.setContentDispositionFormData("attachment", fileName); 	
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(realPath+"\\"+fileName)),    
				                                  headers, HttpStatus.CREATED);
	}
	
	
	/**
	 * 根据课程查询情况
	 * @param model
	 * @param er
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("examResult:viewall")
	@RequestMapping(value="/getsituation",method=RequestMethod.GET)	
	public Map<String, Object> getSituation(Model model,@RequestParam("courseId")long courseId){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
			Map<String, ExamSituation> conditionEntities = examResultService.getConditionEntities(courseId);
			data.put("page", "");
			resultMap.put("data", conditionEntities);
			resultMap.put("msg", "下载成功");
			resultMap.put("successs", true);
		} catch (Exception e) {
			data.put("page", "");
			resultMap.put("data", data);
			resultMap.put("msg", "下载失败");
			resultMap.put("successs", false);
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
}