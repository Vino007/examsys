package com.cnc.exam.course.service;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseCategory;
import com.cnc.exam.course.exception.CourseCategoryDuplicateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyn on 2016/7/26.
 */
public interface CourseCategoryService extends BaseService<CourseCategory,Long> {
    void updateCategory(CourseCategory courseCategory) throws CourseCategoryDuplicateException;

    void saveWithCheckDuplicate(CourseCategory courseCategory, User user) throws CourseCategoryDuplicateException;

    Page<CourseCategory> findCourseCatByCondition(Map<String, Object> searchParams,
                                                  Pageable pageable);
    CourseCategory findByName(String name);

    List<Course> findCoursesByCatID(Long catId);
}
