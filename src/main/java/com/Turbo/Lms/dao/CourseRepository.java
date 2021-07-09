package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    List<Course> findAll();

    Optional<Course> findById(long id);

    void save(Course course);

    void delete(long id);

    List<Course> findByTitleWithPrefix(String prefix);
}