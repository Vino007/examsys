package com.cnc.exam.course.controller;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseCategory;
import com.cnc.exam.course.entity.CourseMessage;
import com.cnc.exam.course.exception.CourseDuplicateException;
import com.cnc.exam.course.service.CourseCategoryService;
import com.cnc.exam.course.service.CourseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/course")
public class CourseController extends BaseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseCategoryService courseCategoryService;

    @ResponseBody
//    @RequiresPermissions("course:menu")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> getALLCourses(Model model, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
        Page<Course> coursePage = courseService.findAll(buildPageRequest(pageNumber));
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("page", coursePage);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:view")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> getCoursesByCondition(Model model, User user, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Page<Course> coursePage = courseService.findCourseByCondition(searchParams, buildPageRequest(pageNumber));
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("page", coursePage);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:create")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> addCourse(Model model, Course course, HttpSession session) {
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            courseService.saveWithCheckDuplicate(course, curUser);
        } catch (CourseDuplicateException e) {
            model.addAttribute("courseDuplicate", "true");
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "课程名重复");
            return resultMap;
        }
        resultMap.put("data", data);
        resultMap.put("msg", "添加成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteCourse(Model model, @RequestParam("deleteIds[]") Long[] deleteIds) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            courseService.delete(deleteIds);
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
    public Map<String, Object> updateCourse(Model model, Course course, Long categoryId) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            courseService.updateCourse(course);
            courseService.setCourseCategory(course.getId(),categoryId);
        } catch (CourseDuplicateException e) {
            model.addAttribute("courseDuplicate", "true");
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "课程名已存在");
            return resultMap;
        }
        resultMap.put("data", data);
        resultMap.put("msg", "更新成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:view")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Map<String, Object> findCourse(Model model, Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("course", courseService.findOne(id));
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:msg")
    @RequestMapping(value = "/addMsg", method = RequestMethod.POST)
    public Map<String, Object> addCourseMsg(Model model, CourseMessage courseMessage, @RequestParam("courseId") Long courseId, HttpSession session) {
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        courseService.addCourseMessage(courseId, courseMessage, curUser);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        resultMap.put("data", data);
        resultMap.put("msg", "评论添加成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:msg")
    @RequestMapping(value = "/showMsg", method = RequestMethod.GET)
    public Map<String, Object> showCourseMsg(Model model, Long id) {
        List<CourseMessage> messages = courseService.showCourseMessage(id);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("messages", messages);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:msg")
    @RequestMapping(value = "/preSetCate", method = RequestMethod.GET)
    public Map<String, Object> prepareSetCategory(Model model, Long id) {
        Course curCourse = courseService.findOne(id);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        List<CourseCategory> categories = courseCategoryService.findAll();
        CourseCategory courseCategory = curCourse.getCourseCategory();
        data.put("currentCategoryName", courseCategory == null ? null : courseCategory.getCoursecatName());
        data.put("availableCategories", categories);
        resultMap.put("data", data);
        resultMap.put("successs", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:msg")
    @RequestMapping(value = "/setCate", method = RequestMethod.GET)
    public Map<String, Object> setCategory(Model model, Long courseId, Long categoryId) {
        courseService.setCourseCategory(courseId, categoryId);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        resultMap.put("data", data);
        resultMap.put("msg", "课程归类成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:msg")
    @RequestMapping(value = "/setOffLine", method = RequestMethod.POST)
    public Map<String, Object> setOffLine(Model model, @RequestParam("ids[]") Long[] ids) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        courseService.offLineCourse(ids);
        resultMap.put("data", data);
        resultMap.put("msg", "下线成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
//    @RequiresPermissions("course:msg")
    @RequestMapping(value = "/setOnLine", method = RequestMethod.POST)
    public Map<String, Object> setOnLine(Model model, @RequestParam("ids[]") Long[] ids) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        courseService.onLineCourse(ids);
        resultMap.put("data", data);
        resultMap.put("msg", "上线成功");
        resultMap.put("success", true);
        return resultMap;
    }
}
