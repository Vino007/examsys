package com.cnc.exam.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.Resource;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.exception.ResourceDuplicateException;
import com.cnc.exam.auth.service.ResourceService;
import com.cnc.exam.auth.service.RoleService;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.auth.utils.Tree;
import com.cnc.exam.auth.utils.TreeUtils;
import com.cnc.exam.base.controller.BaseController;

@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController{
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private RoleService roleService;
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	/**
	 * 根据pageNumber返回对应的page
	 * @param model
	 * @param pageNumber
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("resource:menu")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public Map<String, Object> getAllResources(Model model,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber){	
		Page<Resource> resourcePage=resourceService.findAll(buildPageRequest(pageNumber));
		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", resourcePage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	@ResponseBody
	@RequiresPermissions("resource:view")
	@RequestMapping(value="/find",method=RequestMethod.GET)
	public Map<String, Object> findResource(Model model,Long id){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("resource", resourceService.findOne(id));
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	/**
	 * 返回所有的resource tree
	 */
	@RequiresPermissions("resource:menu")
	@ResponseBody
	@RequestMapping(value="/resourceTree",method=RequestMethod.GET)
	public Map<String, Object> getAllResources(){	
		List<Resource> resources=resourceService.findAll();
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		List<Tree> tree=TreeUtils.fomatResourceToTree(resources);
		data.put("tree", tree);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	@ResponseBody
	@RequiresPermissions("resource:view")
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public Map<String, Object> getRolesByCondition(Model model,Resource resource,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,ServletRequest request){
		Map<String,Object> searchParams=Servlets.getParametersStartingWith(request, "search_");
		Page<Resource> resourcePage=resourceService.findResourceByCondition(searchParams, buildPageRequest(pageNumber));		
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("page", resourcePage);
		resultMap.put("data", data);
		resultMap.put("success", true);
		return resultMap;
	}
	@ResponseBody
	@RequiresPermissions("resource:create")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Map<String, Object> addRole(Model model ,Resource resource,HttpSession session){
		User curUser=(User) session.getAttribute(Constants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			resourceService.saveWithCheckDuplicate(resource,curUser);
			//让admin拥有所有权限
			roleService.connectRoleAndResource(1l,resourceService.findByName(resource.getName()).getId() );//新建一个资源就绑定给超级角色admin，使得admin拥有所有权限
		} catch (ResourceDuplicateException e) {
			resultMap.put("success", false);
			resultMap.put("msg", "用户名重复");
			e.printStackTrace();
			return resultMap;
		}
	
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "添加成功");
		return resultMap;
	}
	@ResponseBody
	@RequiresPermissions("resource:delete")
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public  Map<String, Object> deleteRoles(Model model,@RequestParam("deleteIds[]")Long[] deleteIds){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
		resourceService.delete(deleteIds);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("msg", "删除失败");
			e.printStackTrace();
			return resultMap;
		}

		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "删除成功");
		return resultMap;
		
	}
	@ResponseBody
	@RequiresPermissions("resource:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)	
	public Map<String, Object> updateRole(Model model,Resource resource){
		resourceService.update(resource);
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		resultMap.put("data", data);
		resultMap.put("success", true);
		resultMap.put("msg", "更新成功");
		return resultMap;
		
	}


}
