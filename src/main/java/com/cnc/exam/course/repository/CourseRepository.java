package com.cnc.exam.course.repository;

import com.cnc.exam.base.repository.BaseRepository;
import com.cnc.exam.course.entity.Course;

/**
 * Created by zhangyn on 2016/7/26.
 */
public interface CourseRepository extends BaseRepository<Course,Long> {
    Course findByCourseName(String courseName);
}
