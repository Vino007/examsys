package com.zyn.exam.course.test;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseCategory;
import com.cnc.exam.course.exception.CourseCategoryDuplicateException;
import com.cnc.exam.course.service.CourseCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by zhangyn on 2016/7/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-mvc.xml"})
public class CourseCategoryServiceTest {
    @Autowired
    private CourseCategoryService courseCategoryService;

    @Test
    public void testSaveWithCheckDuplicate() {
        for (int i = 3; i < 9; i++) {
            CourseCategory courseCategory = getCourseCategory();
            courseCategory.setCoursecatName(courseCategory.getCoursecatName()+i);
            User user = getUser();
            try {
                courseCategoryService.saveWithCheckDuplicate(courseCategory, user);
            } catch (CourseCategoryDuplicateException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testShollAll() {
        List<CourseCategory> lst = courseCategoryService.findAll();
        System.out.println("+++++++++++++");
        for (CourseCategory c : lst) {
            System.out.println(c);
        }
        System.out.println("*************");
    }

    @Test
    public void testShollCoursesByCat() {
        List<Course> lst = courseCategoryService.findCoursesByCatID(new Long(1));
        System.out.println("+++++++++++++");
        for (Course c : lst) {
            System.out.println(c);
        }
        System.out.println("*************");
    }

    @Test
    public void testDelete() {
        courseCategoryService.delete(new Long(1));
    }

    public CourseCategory getCourseCategory() {
        CourseCategory courseCategory = new CourseCategory();
        courseCategory.setCoursecatName("cat_");
        courseCategory.setDescription("test description");
        return courseCategory;
    }

    public User getUser() {
        User u = new User();
        u.setUsername("Jon");
        u.setId(new Long(233));
        return u;
    }
}
