package com.cnc.exam.course.entity;

import com.cnc.exam.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by zhangyn on 2016/7/26.
 */
@Entity
@Table(name = "t_courses_message")
public class CourseMessage extends BaseEntity<Long> {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "mess_content",length = 255)
    private String messContent;
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="course_id")
    private Course course;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessContent() {
        return messContent;
    }

    public void setMessContent(String messContent) {
        this.messContent = messContent;
    }
}

