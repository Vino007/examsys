package com.cnc.exam.department.controller;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.department.entity.Department;
import com.cnc.exam.department.exception.DeptDuplicateException;
import com.cnc.exam.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyn on 2016/7/28.
 */
@Controller
@RequestMapping("/department")
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService departmentService;

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> getDepartmentByCondition(Model model, User user, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Page<Department> departmentPage = departmentService.findDeptByCondition(searchParams, buildPageRequest(pageNumber));
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("page", departmentPage);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> addDepartment(Model model, Department department, HttpSession session) {
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            departmentService.saveWithCheckDuplicate(department, curUser);
        } catch (DeptDuplicateException e) {
            model.addAttribute("departmentDuplicate", "true");
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "部门名重复");
            return resultMap;
        }
        resultMap.put("data", data);
        resultMap.put("msg", "添加成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteDepartment(Model model, @RequestParam("deleteIds[]") Long[] deleteIds) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            departmentService.delete(deleteIds);
        } catch (Exception e) {
            resultMap.put("msg", "删除失败");
            resultMap.put("success", false);
        }
        resultMap.put("data", data);
        resultMap.put("msg", "删除成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateDepartment(Model model, Department department) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            departmentService.updateDept(department);
        } catch (DeptDuplicateException e) {
            model.addAttribute("departmentDuplicate", "true");
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "部门名已存在");
            return resultMap;
        }
        resultMap.put("data", data);
        resultMap.put("msg", "更新成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/showUsers", method = RequestMethod.GET)
    public Map<String, Object> showCateCourses(Model model, Long id) {
        List<User> users = departmentService.findUserByDeptId(id);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }
}
