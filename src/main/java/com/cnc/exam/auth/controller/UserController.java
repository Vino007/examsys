package com.cnc.exam.auth.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cnc.exam.department.entity.Department;
import com.cnc.exam.department.service.DepartmentService;
import jxl.read.biff.BiffException;

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
import com.cnc.exam.auth.entity.Role;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.exception.UserDuplicateException;
import com.cnc.exam.auth.service.RoleService;
import com.cnc.exam.auth.service.UserService;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private DepartmentService departmentService;

	@ResponseBody
	@RequiresPermissions("user:menu")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> getALLUsers(
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE + "") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType) {
		
		Page<User> userPage = userService.findAll(buildPageRequest(pageNumber));

		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", userPage);
		resultMap.put("data", data);
		resultMap.put("success", true);

		return resultMap;
	}

	@ResponseBody
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public Map<String, Object> getUsersByCondition(User user,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<User> userPage = userService.findUserByCondition(searchParams, buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", userPage);
		resultMap.put("data", data);
		resultMap.put("success", true);

		return resultMap;
	}

	@ResponseBody
	@RequiresPermissions("user:create")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> addUser( User user,
			@RequestParam(value = "roleIds[]", required = false) Long[] roleIds, Long deptId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			user=userService.saveWithCheckDuplicate(user);
			//userService.clearAllUserAndRoleConnection();
			userService.connectUserAndRole(user.getId(), roleIds);
			userService.connectUserAndDept(user.getId(), deptId);
		} catch (UserDuplicateException e) {
			resultMap.put("success", false);
			resultMap.put("msg", "用户名重复，请重新输入");
			e.printStackTrace();
			return resultMap;
		}
		resultMap.put("success", true);
		resultMap.put("data", data);
		resultMap.put("msg", "添加成功");

		return resultMap;
	}

	@ResponseBody
	@RequiresPermissions("user:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> deleteUsers(@RequestParam("deleteIds[]") Long[] deleteIds) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			userService.clearAllUsersAndDeptConnection(deleteIds);
			userService.delete(deleteIds);
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
	@RequiresPermissions("user:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> updateUser( User user, 
			@RequestParam(value = "roleIds[]", required = false) Long[] roleIds, Long deptId) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			userService.update(user);// 密码这里要做加密处理
			userService.clearAllUserAndRoleConnection(user.getId());
			userService.connectUserAndRole(user.getId(), roleIds);
			userService.connectUserAndDept(user.getId(), deptId);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "更新失败");
			e.printStackTrace();
			return resultMap;
		}

		Map<String, Object> data = new HashMap<>();
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "更新成功");
		return resultMap;

	}

	@ResponseBody
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public Map<String, Object> findById(Long id) {

		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("user", userService.findOne(id));
		resultMap.put("data", data);
		resultMap.put("success", true);

		return resultMap;

	}

	/**
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/getRoles", method = RequestMethod.GET)
	public Map<String, Object> getRolesByUserId(Long id) {

		User user = userService.findOne(id);
		List<Role> roles = roleService.findAll();
		for (Role r : user.getRoles())
			roles.remove(r);

		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("roles", roles);
		resultMap.put("data", data);
		resultMap.put("success", true);

		return resultMap;

	}
	/**
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/getAllRoles", method = RequestMethod.GET)
	public Map<String, Object> getAllRoles() {
		List<Role> roles = roleService.findAll();
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("roles", roles);
		resultMap.put("data", data);
		resultMap.put("success", true);

		return resultMap;

	}
	/*@ResponseBody
	@RequiresPermissions("user:bind")
	@RequestMapping(value = "/bind", method = RequestMethod.POST)
	public Map<String, Object> bind(Model model, @RequestParam("userId") Long userId,
			@RequestParam(value = "roleIds[]", required = false) Long[] roleIds) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			userService.clearAllUserAndRoleConnection(userId);
			userService.connectUserAndRole(userId, roleIds);
		} catch (Exception e) {
			resultMap.put("msg", "绑定失败");
			resultMap.put("success", false);
			return resultMap;
		}
		Page<User> userPage = userService.findAll(buildPageRequest(1));
		data.put("page", userPage);
		resultMap.put("data", data);
		resultMap.put("msg", "绑定成功");
		resultMap.put("success", true);

		return resultMap;

	}*/

	@ResponseBody
	@RequestMapping(value = "/preBindDept", method = RequestMethod.GET)
	public Map<String, Object> prepareBindDept(Model model, Long id) {
		User curUser = userService.findOne(id);
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		List<Department> departments = departmentService.findAll();
		Department department = curUser.getDepartment();
		data.put("currentDeptName", department == null ? null : department.getDeptName());
		data.put("availableDepts", departments);
		resultMap.put("data", data);
		resultMap.put("successs", true);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/bindDept", method = RequestMethod.GET)
	public Map<String, Object> bindDept(Model model, Long userId, Long deptId) {
		userService.connectUserAndDept(userId, deptId);
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		resultMap.put("data", data);
		resultMap.put("msg", "部门绑定成功");
		resultMap.put("success", true);
		return resultMap;
	}

	@ResponseBody
//    @RequiresPermissions("course:msg")
	@RequestMapping(value = "/getAllDept", method = RequestMethod.GET)
	public Map<String, Object> getAllCategory(Model model) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		List<Department> departments = departmentService.findAll();
		data.put("availableDepts", departments);
		resultMap.put("data", data);
		resultMap.put("successs", true);
		return resultMap;
	}
}
