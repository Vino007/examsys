package com.cnc.exam.auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.Resource;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.service.ResourceService;
import com.cnc.exam.auth.service.UserService;
import com.cnc.exam.base.controller.BaseController;

@Controller
public class LoginController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	/**
	 * 登陸失敗
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loginFail")
	public Map<String, Object> fail(HttpServletRequest req, Model model) {
		String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
		String error = null;
		if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
			error = "用户名/密码错误";
		} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
			error = "用户名/密码错误";
		} else if (LockedAccountException.class.getName().equals(exceptionClassName)) {
			error = "账户被锁定，请联系管理员";

		} else if (ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
			error = "密码输入错误超过" + Constants.PASSWORD_RETRY_MAX + "次,请等待一小时后重新尝试";
		} else if (exceptionClassName != null) {
			error = "未知错误";
		}
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("msg", error);
		res.put("success", false);
		return res;
	}

	/**
	 * 登陸成功
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loginSuccess")
	public Map<String, Object> success() {				
		Subject curUser=SecurityUtils.getSubject();
		Session session=curUser.getSession();	
		String username=(String) curUser.getPrincipal();
		User currentUser=userService.findByUsername(username);
		session.setAttribute(Constants.CURRENT_USER, currentUser);//将当前用户放入session
		session.setAttribute(Constants.CURRENT_USERNAME, username);
		List<Resource> menuResources=resourceService.getSidebarResourceByUser(currentUser);//用于前端页面生成侧边栏
		List<Resource> buttonResources=resourceService.getButtonResourceByUser(currentUser);
		Map<String,Object> resourceMap=new HashMap<>();
		Map<String,Object> data=new HashMap<>();
		List<ResourceJson> menuResourceJsons=new ArrayList<>();
		List<ResourceJson> buttonResourceJsons=new ArrayList<>();
		for(Resource resource:menuResources){
			ResourceJson resourceJson=new ResourceJson();
			resourceJson.setName(resource.getName());
			resourceJson.setPermission(resource.getPermission());
			resourceJson.setUrl(resource.getUrl());
			menuResourceJsons.add(resourceJson);
		}
		for(Resource resource:buttonResources){
			ResourceJson resourceJson=new ResourceJson();
			resourceJson.setName(resource.getName());
			resourceJson.setPermission(resource.getPermission());
			resourceJson.setUrl(resource.getUrl());
			buttonResourceJsons.add(resourceJson);
		}
		data.put("menuPermissions", menuResourceJsons);
		data.put("buttonPermissions", buttonResourceJsons);
		resourceMap.put("success", true);
		resourceMap.put("data",data );
		resourceMap.put("msg","登录成功" );
		return resourceMap;
	}
	
	/**
	 * 登陸,返回登陸界面
	 * 
	 * @return
	 */
	//@ResponseBody
	@RequestMapping(value = "/loginPage")
	public String login() {
		return "login";
	}
	
	
	private class ResourceJson{
		private String url;
		private String name;
		private String permission;
		
		public String getPermission() {
			return permission;
		}
		public void setPermission(String permission) {
			this.permission = permission;
		}

		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}

}