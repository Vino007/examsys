package com.cnc.exam.department.service;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.repository.UserRepository;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.department.entity.Department;
import com.cnc.exam.department.exception.DeptDuplicateException;
import com.cnc.exam.department.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyn on 2016/7/28.
 */
@Service("departmentService")
@Transactional
public class DepartmentServiceImpl extends AbstractBaseServiceImpl<Department, Long> implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void delete(Long... ids) {
        Department department;
        for (Long id : ids) {
            department = departmentRepository.findOne(id);
            List<User> users = department.getUsers();
            if (users.size() != 0) {
                for (User user : users) {
                    userRepository.findByUsername(user.getUsername()).setDepartment(null);
                }
            }
            department.getUsers().clear();
            departmentRepository.delete(id);
        }
    }

    @Override
    public void updateDept(Department department) throws DeptDuplicateException {
        Department department1 = departmentRepository.findOne(department.getId());
        String name = department.getDeptName();
        if (departmentRepository.findByDeptName(name) != null && !name.equals(department1.getDeptName())) {
            throw new DeptDuplicateException();
        }
        if (name != null && !name.trim().equals("")) {
            department1.setDeptName(department.getDeptName());
        }
        if (department.getDescription() != null) {
            department1.setDescription(department.getDescription());
        }
        if (department.getAddress() != null) {
            department1.setAddress(department.getAddress());
        }
    }

    @Override
    public void saveWithCheckDuplicate(Department department, User user) throws DeptDuplicateException {
        if (departmentRepository.findByDeptName(department.getDeptName()) != null)
            throw new DeptDuplicateException();
        else {
            department.setCreateTime(new Date());
            department.setCreatorId(user.getId());
            department.setCreatorName(user.getUsername());
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findDeptByCondition(Map<String, Object> searchParams, Pageable pageable) {
        return departmentRepository.findAll(buildSpecification(searchParams), pageable);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Department> buildSpecification(final Map<String, Object> searchParams) {

        Specification<Department> spec = new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate allCondition = null;
                String name = (String) searchParams.get("deptName");
                if (name != null && !"".equals(name)) {
                    Predicate condition = cb.like(root.get("deptName").as(String.class), "%" + searchParams.get("deptName") + "%");
                    if (null == allCondition)
                        allCondition = cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
                    else
                        allCondition = cb.and(allCondition, condition);
                }
                return allCondition;
            }
        };
        return spec;
    }

    @Override
    public Department findByName(String name) {
        return departmentRepository.findByDeptName(name);
    }

    @Override
    public List<User> findUserByDeptId(Long deptId) {
        return departmentRepository.findOne(deptId).getUsers();
    }
}
