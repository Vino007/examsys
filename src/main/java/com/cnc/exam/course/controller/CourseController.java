package com.cnc.exam.course.controller;

import com.cnc.exam.auth.constant.Constants;
import com.cnc.exam.auth.entity.Role;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.service.UserService;
import com.cnc.exam.auth.utils.Servlets;
import com.cnc.exam.base.controller.BaseController;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseCategory;
import com.cnc.exam.course.entity.CourseMessage;
import com.cnc.exam.course.exception.CourseDuplicateException;
import com.cnc.exam.course.exception.DeleteWithMsgException;
import com.cnc.exam.course.service.CourseCategoryService;
import com.cnc.exam.course.service.CourseService;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.log.service.LogsService;
import com.cnc.exam.question.entity.Question;
import com.cnc.exam.question.service.QuestionService;
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
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zhangyn on 2016/7/26.
 */
@Controller
@RequestMapping("/course")
public class CourseController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseCategoryService courseCategoryService;
    @Autowired
    private LogsService logsService;

    @ResponseBody
    @RequiresPermissions("course:menu")
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
    @RequiresPermissions("course:create")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> addCourse(Model model, Course course, Long categoryId, HttpSession session) {
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        User currentUser = userService.findOne(curUser.getId());
        LogsEntity logsEntity = new LogsEntity(currentUser,1,"添加新课程",new Timestamp(new Date().getTime()));
        logsService.save(logsEntity);
        if (!isPermission(curUser, categoryId)) {
            model.addAttribute("Permission denied", "true");
            resultMap.put("success", false);
            resultMap.put("msg", "没有该操作权限");
            return resultMap;
        }
        try {
            course = courseService.saveWithCheckDuplicate(course, curUser);
            courseService.setCourseCategory(course.getId(), categoryId);
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
    @RequiresPermissions("course:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteCourse(Model model, @RequestParam("deleteIds[]") Long[] deleteIds, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        User currentUser = userService.findOne(curUser.getId());
        LogsEntity logsEntity = new LogsEntity(currentUser,1,"删除课程",new Timestamp(new Date().getTime()));
        logsService.save(logsEntity);
        for (Long couId : deleteIds) {
            if (courseService.findOne(couId).getCourseCategory() != null && !isPermission(curUser, courseService.findOne(couId).getCourseCategory().getId())) {
                model.addAttribute("Permission denied", "true");
                resultMap.put("success", false);
                resultMap.put("msg", "没有该操作权限");
                return resultMap;
            }
        }
        try {
            logsEntity = new LogsEntity(currentUser,1,"删除课程对应题库",new Timestamp(new Date().getTime()));

            logsService.save(logsEntity);
            courseService.deleteCourses(deleteIds);
        } catch (DeleteWithMsgException e) {
            resultMap.put("msg", "部分课程存在留言或考试");
            resultMap.put("success", false);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("msg", "删除失败");
            resultMap.put("success", false);
            return resultMap;
        }
        resultMap.put("data", data);
        resultMap.put("msg", "删除成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequiresPermissions("course:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateCourse(Model model, Course course, Long categoryId, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        User currentUser = userService.findOne(curUser.getId());
        LogsEntity logsEntity = new LogsEntity(currentUser,1,"更新课程",new Timestamp(new Date().getTime()));
        logsService.save(logsEntity);
        if (categoryId!= null && !isPermission(curUser, categoryId)) {
            model.addAttribute("Permission denied", "true");
            resultMap.put("success", false);
            resultMap.put("msg", "没有该操作权限");
            return resultMap;
        }
        try {
            courseService.updateCourse(course);
            logsEntity = new LogsEntity(currentUser,1,"修改课程的分类",new Timestamp(new Date().getTime()));
            logsService.save(logsEntity);
            courseService.setCourseCategory(course.getId(), categoryId);
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
    @RequestMapping(value = "/addMsg", method = RequestMethod.POST)
    public Map<String, Object> addCourseMsg(Model model, CourseMessage courseMessage, @RequestParam("courseId") Long courseId, HttpSession session) {
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        User currentUser = userService.findOne(curUser.getId());
        LogsEntity logsEntity = new LogsEntity(currentUser,1,"用户添加课程留言",new Timestamp(new Date().getTime()));
        logsService.save(logsEntity);
        courseService.addCourseMessage(courseId, courseMessage, curUser);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        resultMap.put("data", data);
        resultMap.put("msg", "评论添加成功");
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
    @RequiresPermissions("course:viewMessage")
    @RequestMapping(value = "/showMsg", method = RequestMethod.GET)
    public Map<String, Object> showCourseMsg(Model model, Long id, HttpSession session) {
        User curUser = (User) session.getAttribute(Constants.CURRENT_USER);
        User currentUser = userService.findOne(curUser.getId());
        LogsEntity logsEntity = new LogsEntity(currentUser,1,"查看课程留言",new Timestamp(new Date().getTime()));
        logsService.save(logsEntity);
        List<CourseMessage> messages = courseService.showCourseMessage(id);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("messages", messages);
        resultMap.put("data", data);
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
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
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
        resultMap.put("success", true);
        return resultMap;
    }

    @ResponseBody
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

    @ResponseBody
    @RequestMapping(value = "/getCourseQues", method = RequestMethod.GET)
    public Map<String, Object> getCourseQues(Model model, Long id) {
        Course curCourse = courseService.findOne(id);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        List<Question> questions = curCourse.getQuestions();
        data.put("questions", questions);
        resultMap.put("data", data);
        resultMap.put("success", true);
        return resultMap;
    }

    Boolean isPermission(User user, Long categoryId) {
        if(categoryId == null)
            return true;
        Set<Role> roles = userService.findOne(user.getId()).getRoles();
        for (Role role : roles) {
            if (role.getName().equals("admin"))
                return true;
            Set<CourseCategory> categories = role.getCategories();
            for (CourseCategory c : categories) {
                if (categoryId == c.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
