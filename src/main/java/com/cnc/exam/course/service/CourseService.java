package com.cnc.exam.course.service;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseMessage;
import com.cnc.exam.course.exception.CourseDuplicateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyn on 2016/7/26.
 */
public interface CourseService extends BaseService<Course, Long> {

    void update(Course course);

    Course findByName(String name);

    void saveWithCheckDuplicate(Course resource, User user) throws CourseDuplicateException;

    void addCourseMessage(Long courseId, CourseMessage courseMessage, User user);

    List<CourseMessage> showCourseMessage(Long couId);

    void setCourseCategory(Long courseId, Long categoryId);

    void removeCourseCategory(Long courseId);

    void offLineCourse(Long... ids);

    void onLineCourse(Long... ids);

    Page<Course> findCourseByCondition(Map<String, Object> searchParams,
                                       Pageable pageable);
}

