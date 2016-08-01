package com.cnc.exam.course.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cnc.exam.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyn on 2016/7/26.
 */
@Entity
@Table(name = "t_courses")
public class Course extends BaseEntity<Long> {

    @Column(name = "course_name", length = 30)
    private String courseName;
    @Column(name = "course_type")
    private Boolean courseType = Boolean.FALSE;
    @Column(name = "outline", length = 255)
    private String outline;
    @Column(name = "objectives", length = 255)
    private String objectives;
    @Column(name = "teacher", length = 30)
    private String teacher;
    @Column(name = "online_data", length = 255)
    private String onlineData;
    @Column(name = "is_online")
    private Boolean isOnline = Boolean.FALSE;
    @JSONField(serialize = false)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "course")
    private List<CourseMessage> courseMessages = new ArrayList<CourseMessage>();
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coursecat_id")
    private CourseCategory courseCategory;

    public CourseCategory getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategory courseCategory) {
        this.courseCategory = courseCategory;
    }

    public List<CourseMessage> getCourseMessages() {
        return courseMessages;
    }

    public void setCourseMessages(List<CourseMessage> courseMessages) {
        this.courseMessages = courseMessages;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Boolean getCourseType() {
        return courseType;
    }

    public void setCourseType(Boolean courseType) {
        this.courseType = courseType;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getOnlineData() {
        return onlineData;
    }

    public void setOnlineData(String onlineData) {
        this.onlineData = onlineData;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }
}

