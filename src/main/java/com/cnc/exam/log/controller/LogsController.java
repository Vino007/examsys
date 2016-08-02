package com.cnc.exam.log.controller;

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
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.log.service.LogsService;

@Controller
@RequestMapping("/logs")
public class LogsController extends BaseController {

	@Autowired
	private LogsService logsService;
	
	/**
	 * 分页返回日志数据
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> getALLLogs(Model model,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE + "") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType) {
		
		Page<LogsEntity> logsPage = logsService.findAll(buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", logsPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
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
	public Map<String, Object> getLogsByCondition(Model model, LogsEntity logs,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<LogsEntity> logsPage = logsService.findLogsByCondition(searchParams, buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", logsPage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
}
