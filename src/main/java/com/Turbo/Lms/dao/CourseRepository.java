package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.dto.LessonDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleLike(String title);
    @Query("select new com.Turbo.Lms.dto.CourseDto(c.id, c.author, c.title) " +
            "from Course c where c.id = :id")
    CourseDto findByIdAndConvertToDto(@Param("id") long id);
}