package com.cnc.exam.result.controller;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.log.utils.FastJsonTool;
import com.cnc.exam.result.entity.ExamResultEntity;
import com.cnc.exam.result.service.ExamResultService;

@Controller
@RequestMapping("/examresult")
public class ExamResultController extends BaseController {

	@Autowired
	private ExamResultService examResultService;
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public Map<String, Object> insertER(Model model, ExamResultEntity examResultEntity){
		Map<String, Object> resultMap = new HashMap<>();
		try{
			examResultEntity.setCreateTime(new Date());
			examResultService.save(examResultEntity);
			resultMap.put("data", "");
			resultMap.put("successs", true);
			resultMap.put("msg", "添加成功");
		}catch(Exception e){
			resultMap.put("data", "");
			resultMap.put("successs", false);
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
		Page<ExamResultEntity> logsPage = examResultService.findERByCondition(searchParams, buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", logsPage);
		resultMap.put("data", data);
		resultMap.put("successs", true);
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
	@RequestMapping(value="/delete",method=RequestMethod.GET)
	public  Map<String, Object> deleteER(Model model,@RequestParam("deleteIds[]")Long[] deleteIds){
		Map<String, Object> resultMap = new HashMap<>();
		try{
			examResultService.delete(deleteIds);
		}catch(Exception e){
			resultMap.put("msg", "删除失败");
			resultMap.put("successs", false);
		}
		resultMap.put("data", "");
		resultMap.put("msg", "删除成功");
		resultMap.put("successs", true);
		return resultMap;
	}
	
	/**
	 * 更新成绩
	 * @param model
	 * @param er
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/update",method=RequestMethod.GET)	
	public Map<String, Object> updateExamResult(Model model,ExamResultEntity er){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		examResultService.update(er);
		data.put("page", "");
		resultMap.put("data", data);
		resultMap.put("msg", "更新成功");
		resultMap.put("successs", true);
		return resultMap;
	}
	
	/**
	 * 导出成绩
	 * @param model
	 * @param er
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/download ",method=RequestMethod.GET)	
	public Map<String, Object> downloadEaxmResult(Model model,@RequestParam("path")String path){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			examResultService.saveToExcel(path);
			data.put("page", "");
			resultMap.put("data", data);
			resultMap.put("msg", "下载成功");
			resultMap.put("successs", true);
		} catch (FileNotFoundException e) {
			data.put("page", "");
			resultMap.put("data", data);
			resultMap.put("msg", "下载失败");
			resultMap.put("successs", false);
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
}