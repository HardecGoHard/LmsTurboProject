package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.LessonDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("select new com.Turbo.Lms.dto.LessonDto(l.id, l.title, l.course.id) " +
            "from Lesson l where l.course.id = :id")
    List<LessonDto> findAllForLessonIdWithoutText(@Param("id") long id);
    List<Lesson> findLessonsByCourse_Id(Long course_id);
}
