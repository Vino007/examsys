package com.cnc.exam.course.controller;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseCategory;
import com.cnc.exam.course.exception.CourseCategoryDuplicateException;
import com.cnc.exam.course.service.CourseCategoryService;
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
 * Created by zhangyn on 2016/7/26.
 */
@Controller
@RequestMapping("/coursecat")
public class CourseCategoryController extends BaseController{
    @Autowired
    private CourseCategoryService courseCategoryService;

    @ResponseBody
//    @RequiresPermissions("coursecat:menu")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> getALLCategories(Model model, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
        Page<CourseCategory> categoryPage = courseCategoryService.findAll(buildPageRequest(pageNumber));
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("page", categoryPage);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> getCourseCatsByCondition(Model model, User user, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Page<CourseCategory> categoryPage = courseCategoryService.findCourseCatByCondition(searchParams, buildPageRequest(pageNumber));
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("page", categoryPage);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:create")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> addCourseCate(Model model, CourseCategory category, HttpSession session) {
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            courseCategoryService.saveWithCheckDuplicate(category, curUser);
        } catch (CourseCategoryDuplicateException e) {
            model.addAttribute("courseCateDuplicate", "true");
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "分类名重复");
            return resultMap;
        }
        resultMap.put("data", data);
        resultMap.put("msg", "添加成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteCourseCate(Model model, @RequestParam("deleteIds[]") Long[] deleteIds) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            courseCategoryService.delete(deleteIds);
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
//    @RequiresPermissions("course:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateCourseCate(Model model, CourseCategory category) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            courseCategoryService.updateCategory(category);
        } catch (CourseCategoryDuplicateException e) {
            model.addAttribute("categoryDuplicate", "true");
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "分类名已存在");
            return resultMap;
        }
        resultMap.put("data", data);
        resultMap.put("msg", "更新成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:msg")
    @RequestMapping(value = "/showCourses", method = RequestMethod.GET)
    public Map<String, Object> showCateCourses(Model model, Long id) {
        List<Course> courseList = courseCategoryService.findCoursesByCatID(id);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("courses", courseList);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }
}
