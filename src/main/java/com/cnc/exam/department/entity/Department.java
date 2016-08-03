package com.cnc.exam.department.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyn on 2016/7/28.
 */
@Entity
@Table(name = "t_dept")
public class Department extends BaseEntity<Long> {

    @Column(name = "dept_name", length = 20)
    private String deptName;
    @Column(name = " address", length = 100)
    private String address;
    @Column(name = "description", length = 255)
    private String description;
    @JSONField(serialize = false)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "department")
    private List<User> users = new ArrayList<User>();

    public Department() {
    }

    public Department(String deptName, String address, String description) {
        this.deptName = deptName;
        this.address = address;
        this.description = description;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
