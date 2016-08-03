package com.cnc.exam.course.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cnc.exam.auth.entity.Role;
import com.cnc.exam.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JSONField(serialize = false)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "courseCategory")
    private Set<Course> courses = new HashSet<>();

    @JSONField(serialize=false)
    @ManyToMany(cascade = CascadeType.ALL,mappedBy="categories",targetEntity=Role.class)
    private Set<Role> roles=new HashSet<Role>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
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

