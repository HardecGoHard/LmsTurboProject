package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService  {
    private CourseRepository courseRepository;

    @Autowired
    private CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course findById(long id) {
        return courseRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Произошла ошибка: курс не найден ")
        );
    }
    public CourseDto findByIdAndConvertToDto(Long id){
        CourseDto courseDto =  courseRepository.findByIdAndConvertToDto(id);
        if (courseDto == null) {
            throw new NotFoundException("Произошла ошибка: курс не найден");
        }
       return courseDto;
    }

    public void save(Course course) {
        courseRepository.save(course);
    }
    public void delete(Course course) {
        courseRepository.delete(course);
    }

    public List<Course> findByTitleLike(String prefix) {
        return courseRepository.findByTitleLike(prefix);
    }

    public List<Course> findCoursesNotAssignToUser(Long userId, String title){
        return courseRepository.findCoursesByTitleNotAssignToUser(userId, title);
    }

}

