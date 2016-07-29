package com.cnc.exam.course.service;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseCategory;
import com.cnc.exam.course.exception.CourseCategoryDuplicateException;
import com.cnc.exam.course.repository.CourseCategoryRepository;
import com.cnc.exam.course.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyn on 2016/7/26.
 */
@Service("courseCategoryService")
@Transactional
public class CourseCategoryServiceImpl extends AbstractBaseServiceImpl<CourseCategory, Long> implements CourseCategoryService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryRepository getCourseCategoryRepository() {
        return courseCategoryRepository;
    }

    public void setCourseCategoryRepository(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public void delete(Long... ids) {
        CourseCategory courseCategory;
        for (Long id : ids) {
            courseCategory = courseCategoryRepository.findOne(id);
            List<Course> courses = courseCategory.getCourses();
            if (courses.size() != 0) {
                for (Course course : courses) {
                    courseRepository.findByCourseName(course.getCourseName()).setCourseCategory(null);
                }
            }
            courseCategory.getCourses().clear();
            courseCategoryRepository.delete(id);
        }
    }

    @Override
    public void updateCategory(CourseCategory courseCategory) throws CourseCategoryDuplicateException {
        CourseCategory courseCategory1 = courseCategoryRepository.findOne(courseCategory.getId());
        String name = courseCategory.getCoursecatName();
        if (courseCategoryRepository.findByCoursecatName(name) != null && !name.equals(courseCategory1.getCoursecatName())) {
            throw new CourseCategoryDuplicateException();
        }
        if (name != null && !name.trim().equals("")) {
            courseCategory1.setCoursecatName(courseCategory.getCoursecatName());
        }
        if (courseCategory.getDescription() != null) {
            courseCategory1.setDescription(courseCategory.getDescription());
        }
    }

    @Override
    public void saveWithCheckDuplicate(CourseCategory courseCategory, User user) throws CourseCategoryDuplicateException {
        if (courseCategoryRepository.findByCoursecatName(courseCategory.getCoursecatName()) != null)
            throw new CourseCategoryDuplicateException();
        else {
            courseCategory.setCreateTime(new Date());
            courseCategory.setCreatorId(user.getId());
            courseCategory.setCreatorName(user.getUsername());
            courseCategoryRepository.save(courseCategory);
        }
    }

    @Override
    public Page<CourseCategory> findCourseCatByCondition(Map<String, Object> searchParams, Pageable pageable) {
        return courseCategoryRepository.findAll(buildSpecification(searchParams), pageable);
    }

    @Override
    public CourseCategory findByName(String name) {
        return courseCategoryRepository.findByCoursecatName(name);
    }

    @Override
    public List<Course> findCoursesByCatID(Long catId) {
        CourseCategory courseCategory = courseCategoryRepository.findOne(catId);
        return courseCategory.getCourses();
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<CourseCategory> buildSpecification(final Map<String, Object> searchParams) {

        Specification<CourseCategory> spec = new Specification<CourseCategory>() {
            @Override
            public Predicate toPredicate(Root<CourseCategory> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate allCondition = null;
                String name = (String) searchParams.get("coursecatName");
                if (name != null && !"".equals(name)) {
                    Predicate condition = cb.like(root.get("coursecatName").as(String.class), "%" + searchParams.get("coursecatName") + "%");
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
}
