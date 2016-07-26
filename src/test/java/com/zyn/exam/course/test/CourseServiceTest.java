package com.zyn.exam.course.test;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseMessage;
import com.cnc.exam.course.exception.CourseDuplicateException;
import com.cnc.exam.course.service.CourseService;
import org.junit.*;
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
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @Test
    public void testSaveWithCheckDuplicate() {
        User user = getUser();
        for(int i =5;i<13;i++) {
            Course course = getCourse();
            course.setCourseName(course.getCourseName() + i);
            try {
                courseService.saveWithCheckDuplicate(course, user);
            } catch (CourseDuplicateException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testUpdate() {
        Course course = getCourse();
        course.setId(new Long(9));
        course.setObjectives("object update");
        course.setCourseName("course update");
        courseService.update(course);
    }

    @Test
    public void testFindByName() {
        Course course = courseService.findByName("course update");
        System.out.println(course);
    }

    @Test
    public void testOffline() {
        courseService.offLineCourse(new Long(10), new Long(11));
    }

    @Test
    public void testOnline() {
        courseService.onLineCourse(new Long(10), new Long(11));
    }

    @Test
    public void testFindAllCourse() {
        List<Course> courses = courseService.findAll();
        System.out.println("+++++++++++++");
        for (Course c : courses) {
            System.out.println(c);
        }
        System.out.println("*************");
    }

    @Test
    public void testAddCourseMsg() {
        CourseMessage cm = getCourseMessage();
        cm.setMessContent("again msg");
        User user = getUser();
        courseService.addCourseMessage(new Long(16), cm, user);
    }

    @Test
    public void testShowCourseMessage() {
        List<CourseMessage> messages = courseService.showCourseMessage(new Long(13));
        System.out.println("+++++++++++++");
        for (CourseMessage c : messages) {
            System.out.println(c);
        }
        System.out.println("*************");
    }

    @Test
    public void testAddCategoyr(){
        courseService.setCourseCategory(new Long(16),new Long(1));
        courseService.setCourseCategory(new Long(17),new Long(1));
//        courseService.setCourseCategory(new Long(6),new Long(1));
//        courseService.setCourseCategory(new Long(8),new Long(1));
    }

    @Test
    public void testRemoveCate(){
        courseService.removeCourseCategory(new Long(12));
    }

    @Test
    public void testDelete(){
        courseService.delete(new Long(16));
    }

    public CourseMessage getCourseMessage() {
        CourseMessage cm = new CourseMessage();
        cm.setMessContent("test message");
        return cm;
    }

    public Course getCourse() {
        Course c = new Course();
        c.setCourseName("test course");
        c.setObjectives("test Object");
        c.setOutline("test outline");
        c.setCourseType(false);
        c.setOnlineData("URL:");
        c.setOnline(true);
        return c;
    }

    public User getUser() {
        User u = new User();
        u.setUsername("Jon");
        u.setId(new Long(233));
        return u;
    }
}
