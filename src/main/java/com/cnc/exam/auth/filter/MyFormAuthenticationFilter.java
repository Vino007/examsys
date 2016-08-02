package com.cnc.exam.auth.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.Resource;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.service.ResourceService;
import com.cnc.exam.auth.service.UserService;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter{
	@Autowired
	private UserService userService;
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private ResourceService resourceService;
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		User curUser=userService.findByUsername((String)subject.getPrincipal());
		//设置登录时间和上次登录时间
		 if(curUser.getLoginTime()!=null){
		        curUser.setLastLoginTime(curUser.getLoginTime());
		 }
		curUser.setLoginTime(new Date());	
		userService.update(curUser);
		httpServletResponse.setCharacterEncoding("UTF-8");
		PrintWriter out = httpServletResponse.getWriter();
		//no session 解决方案 start
		EntityManagerFactory emf;//用於事務操作
		emf = (EntityManagerFactory) wac.getBean("entityManagerFactory");
		EntityManager em = emf.createEntityManager();
		EntityManagerHolder emHolder = new EntityManagerHolder(em);
		TransactionSynchronizationManager.bindResource(emf, emHolder);
		
		Map<String,Object> resultMap=loginSuccessMessage();			
		
		TransactionSynchronizationManager.unbindResource(emf);
		
		//no session 解决方案 end
		out.println(JSON.toJSONString(resultMap));
		out.flush();
		out.close();
		return false;
	}
	@Override
	protected boolean executeLogin(ServletRequest arg0, ServletResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		return super.executeLogin(arg0, arg1);
	}
	
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		Map<String,Object> resultMap=new HashMap<>();
		Map<String,Object> data=new HashMap<>();
		try {		
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String message = e.getClass().getSimpleName();
			if ("IncorrectCredentialsException".equals(message)) {
				resultMap.put("msg","密码错误" );
			} else if ("UnknownAccountException".equals(message)) {
				resultMap.put("msg","账号不存在" );
			} else if ("LockedAccountException".equals(message)) {
				resultMap.put("msg","账号被锁定" );
			} else {
				resultMap.put("msg","未知错误" );
			}

			resultMap.put("success", false);
			resultMap.put("data",data );
			out.println(JSON.toJSONString(resultMap));
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
	public Map<String,Object> loginSuccessMessage(){
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
		data.put("userId", currentUser.getId());
		resourceMap.put("success", true);
		resourceMap.put("data",data );
		resourceMap.put("msg","登录成功" );
		return resourceMap;
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
