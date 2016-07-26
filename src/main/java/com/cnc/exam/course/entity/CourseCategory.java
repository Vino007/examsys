package com.cnc.exam.course.entity;

import com.cnc.exam.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyn on 2016/7/26.
 */
@Entity
@Table(name = "t_course_category")
public class CourseCategory extends BaseEntity<Long> {

    @Column(name = "coursecat_name", length = 30)
    private String coursecatName;
    @Column(name = "description", length = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "courseCategory")
    private List<Course> courses = new ArrayList<Course>();

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getCoursecatName() {
        return coursecatName;
    }

    public void setCoursecatName(String coursecatName) {
        this.coursecatName = coursecatName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

