package com.cnc.exam.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.User;
@Controller

public class AuthorizedController {
	@ResponseBody

	@RequestMapping(value = "/unauthorized")
	public Map<String, Object> unauthorized() {
		
		

		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		resultMap.put("data", data);
		resultMap.put("msg", "您没有权限进行操作");
		resultMap.put("success", false);

		return resultMap;
	}
}
