package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.domain.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private CourseRepository courseRepository;

    @Autowired
    private CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(long id) {
        return courseRepository.findById(id);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public void delete(long id) {
        courseRepository.delete(id);
    }

    public List<Course> findByTitleWithPrefix(String prefix) {
        return courseRepository.findByTitleWithPrefix(prefix);
    }
}

