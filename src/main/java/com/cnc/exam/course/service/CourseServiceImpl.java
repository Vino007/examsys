package com.cnc.exam.course.service;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.course.entity.CourseMessage;
import com.cnc.exam.course.exception.CourseDuplicateException;
import com.cnc.exam.course.repository.CourseCategoryRepository;
import com.cnc.exam.course.repository.CourseMessageRepository;
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
@Service("courseService")
@Transactional
public class CourseServiceImpl extends AbstractBaseServiceImpl<Course, Long> implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseMessageRepository courseMessageRepository;
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void delete(Long... ids) {
        for (Long id : ids) {
            Course c = courseRepository.findOne(id);
            courseCategoryRepository.findOne(c.getCourseCategory().getId()).getCourses().remove(c);
            c.setCourseCategory(null);
            courseRepository.delete(id);
        }
    }

    @Override
    public void update(Course course) {
        Course course2 = courseRepository.findOne(course.getId());
        if (course.getCourseName() != null && !course.getCourseName().trim().equals("")) {
            course2.setCourseName(course.getCourseName());
        }
        if (course.getCourseType() != null) {
            course2.setCourseType(course.getCourseType());
        }
        if (course.getObjectives() != null) {
            course2.setObjectives(course.getObjectives());
        }
        if (course.getTeacher() != null) {
            course2.setTeacher(course.getTeacher());
        }
        if (course.getOnlineData() != null) {
            course2.setOnlineData(course.getOnlineData());
        }
        if (course.getOutline() != null) {
            course2.setOutline(course.getOutline());
        }
    }

    @Override
    public Course findByName(String name) {
        return courseRepository.findByCourseName(name);
    }

    @Override
    public void saveWithCheckDuplicate(Course course, User user) throws CourseDuplicateException {
        if (courseRepository.findByCourseName(course.getCourseName()) != null)
            throw new CourseDuplicateException();
        else {
            course.setCreateTime(new Date());
            //创建人id
            course.setCreatorName(user.getUsername());
            courseRepository.save(course);
        }
    }

    @Override
    public void addCourseMessage(Long courseId, CourseMessage courseMessage, User user) {
        courseMessage.setUserId(user.getId());
        courseMessage.setCreateTime(new Date());
        courseMessage.setCreatorName(user.getUsername());
        courseMessage.setCourse(courseRepository.findOne(courseId));
        courseMessageRepository.save(courseMessage);
    }

    @Override
    public List<CourseMessage> showCourseMessage(Long couId) {
        return courseRepository.findOne(couId).getCourseMessages();
    }

    @Override
    public void setCourseCategory(Long courseId, Long categoryId) {
        courseRepository.findOne(courseId).setCourseCategory(courseCategoryRepository.findOne(categoryId));
    }

    @Override
    public void removeCourseCategory(Long courseId) {
        courseRepository.findOne(courseId).setCourseCategory(null);
    }

    @Override
    public void offLineCourse(Long... ids) {
        for (Long id : ids)
            courseRepository.findOne(id).setOnline(false);
    }

    @Override
    public void onLineCourse(Long... ids) {
        for (Long id : ids)
            courseRepository.findOne(id).setOnline(true);
    }

    @Override
    public Page<Course> findCourseByCondition(Map<String, Object> searchParams, Pageable pageable) {
        return courseRepository.findAll(buildSpecification(searchParams), pageable);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Course> buildSpecification(final Map<String, Object> searchParams) {

        Specification<Course> spec = new Specification<Course>() {
            @Override
            public Predicate toPredicate(Root<Course> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate allCondition = null;
                String name = (String) searchParams.get("courseName");
                String createTimeRange = (String) searchParams.get("createTimeRange");
                if (name != null && !"".equals(name)) {
                    Predicate condition = cb.like(root.get("courseName").as(String.class), "%" + searchParams.get("name") + "%");
                    if (null == allCondition)
                        allCondition = cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
                    else
                        allCondition = cb.and(allCondition, condition);
                }


                if (createTimeRange != null && !"".equals(createTimeRange)) {
                    String createTimeStartStr = createTimeRange.split(" - ")[0] + ":00:00:00";
                    String createTimeEndStr = createTimeRange.split(" - ")[1] + ":23:59:59";
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy:hh:mm:ss");
                    System.out.println(createTimeStartStr);
                    try {
                        Date createTimeStart = format.parse(createTimeStartStr);
                        Date createTimeEnd = format.parse(createTimeEndStr);
                        Predicate condition = cb.between(root.get("createTime").as(Date.class), createTimeStart, createTimeEnd);
                        if (null == allCondition)
                            allCondition = cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
                        else
                            allCondition = cb.and(allCondition, condition);

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Logger log = LoggerFactory.getLogger(this.getClass());
                        log.error("createTime 转换时间出错");
                    }


                }
                return allCondition;
            }

        };
        return spec;
    }
}

