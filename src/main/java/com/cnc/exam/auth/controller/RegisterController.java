package com.cnc.exam.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.exception.UserDuplicateException;
import com.cnc.exam.auth.service.UserService;
import com.cnc.exam.base.controller.BaseController;
@Controller
public class RegisterController extends BaseController{
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/prepareRegister",method=RequestMethod.GET)
	public String prepareRegister(){
		return "register";
	}
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String register(Model model,User user){
		try {
			userService.saveWithCheckDuplicate(user);
		} catch (UserDuplicateException e) {
			model.addAttribute("isUserDuplicate", "用户名已被使用");
			return "register";
			
		}
		return "redirect:/login";
		
	}

}
