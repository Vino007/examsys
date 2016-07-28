package com.cnc.exam.course.repository;

import com.cnc.exam.base.repository.BaseRepository;
import com.cnc.exam.course.entity.CourseCategory;

/**
 * Created by zhangyn on 2016/7/26.
 */
public interface CourseCategoryRepository extends BaseRepository<CourseCategory,Long> {
    CourseCategory findByCoursecatName(String coursecatName);
}