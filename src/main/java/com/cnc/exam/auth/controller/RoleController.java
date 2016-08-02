package com.cnc.exam.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import com.cnc.exam.course.entity.CourseCategory;
import com.cnc.exam.course.service.CourseCategoryService;
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
import com.cnc.exam.auth.entity.Resource;
import com.cnc.exam.auth.entity.Role;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.exception.RoleDuplicateException;
import com.cnc.exam.auth.service.ResourceService;
import com.cnc.exam.auth.service.RoleService;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.auth.utils.Tree;
import com.cnc.exam.auth.utils.TreeUtils;
import com.cnc.exam.base.controller.BaseController;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private CourseCategoryService courseCategoryService;
	
	@ResponseBody
	@RequiresPermissions("role:menu")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public Map<String, Object> getAllRoles(Model model,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber){	
		Page<Role> rolePage=roleService.findAll(buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", rolePage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	
	@ResponseBody
	@RequiresPermissions("role:view")
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public Map<String, Object> getRolesByCondition(Model model,Role role,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,ServletRequest request){
		Map<String,Object> searchParams=Servlets.getParametersStartingWith(request, "search_");
		Page<Role> rolePage=roleService.findRoleByCondition(searchParams, buildPageRequest(pageNumber));
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", rolePage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	@ResponseBody
	@RequiresPermissions("role:create")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Map<String, Object> addRole(Model model ,Role role,HttpSession session){
		User curUser=(User) session.getAttribute(Constants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			roleService.saveWithCheckDuplicate(role,curUser);
			
		} catch (RoleDuplicateException e) {
			model.addAttribute("roleDuplicate", "true");
			e.printStackTrace();
			resultMap.put("success", false);
			resultMap.put("msg", "角色名重复");
		}
		resultMap.put("data", data);
		resultMap.put("msg", "添加成功");
		resultMap.put("success", true);
		return resultMap;	
	}
	@ResponseBody
	@RequiresPermissions("role:delete")
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public  Map<String, Object> deleteRoles(Model model,@RequestParam("deleteIds[]")Long[] deleteIds){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try{
		roleService.delete(deleteIds);
		}catch(Exception e){
			resultMap.put("msg", "删除失败");
			resultMap.put("success", false);
		}
		resultMap.put("data", data);
		resultMap.put("msg", "删除成功");
		resultMap.put("success", true);
		return resultMap;
		
	}
	@ResponseBody
	@RequiresPermissions("role:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)	
	public Map<String, Object> updateRole(Model model,Role role){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		roleService.update(role);
		resultMap.put("data", data);
		resultMap.put("msg", "更新成功");
		resultMap.put("success", true);
		return resultMap;
		
	}	
	@ResponseBody
	@RequiresPermissions("role:view")
	@RequestMapping(value="/find",method=RequestMethod.GET)
	public Map<String, Object> findRole(Model model,Long id){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("role", roleService.findOne(id));
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
		
	}

	@RequiresPermissions("role:bind")
	@ResponseBody
	@RequestMapping(value="/getResourceTree",method=RequestMethod.GET)
	public Map<String, Object> getResourcesByRole(Long id){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		List<Resource> allResources=resourceService.findAll();
		Role role=roleService.findOne(id);
		Set<Resource> checkedResources=role.getResources();
		List<Resource> unCheckedResources=resourceService.findAll();
		for(Resource res:checkedResources){
			if(allResources.contains(res))
				unCheckedResources.remove(res);
		}
		List<Tree> tree=TreeUtils.fomatResourceToTree(unCheckedResources,checkedResources);
		data.put("tree", tree);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	
		
	}
	@ResponseBody
	@RequiresPermissions("role:bind")
	@RequestMapping(value="/bind",method=RequestMethod.POST)
	public Map<String, Object> bind(Model model,@RequestParam("roleId")Long roleId,@RequestParam(value="resourceIds[]",required=false)Long[] resourceIds){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		roleService.clearAllRoleAndResourceConnection(roleId);
		try{
			roleService.connectRoleAndResource(roleId,resourceIds);
		}catch(Exception e){
			resultMap.put("msg", "绑定失败");
			resultMap.put("success", false);
			return resultMap;
		}
	
		resultMap.put("data", data);
		resultMap.put("msg", "绑定成功");
		resultMap.put("success", true);
		return resultMap;
		
	}

	@ResponseBody
	@RequestMapping(value = "/getAllCat", method = RequestMethod.GET)
	public Map<String, Object> getAllCategory(Model model) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		List<CourseCategory> categories = courseCategoryService.findAll();
		data.put("availableCategories", categories);
		resultMap.put("data", data);
		resultMap.put("successs", true);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value="/bindCats",method=RequestMethod.POST)
	public Map<String, Object> bindCategories(Model model,@RequestParam("roleId")Long roleId,@RequestParam(value="categoryIds[]",required=false)Long[] categoryIds){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		roleService.clearAllRoleAndCategoryConnection(roleId);
		try{
			roleService.connectRoleAndCategory(roleId,categoryIds);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("msg", "绑定失败");
			resultMap.put("success", false);
			return resultMap;
		}

		resultMap.put("data", data);
		resultMap.put("msg", "绑定成功");
		resultMap.put("success", true);
		return resultMap;

	}
}
