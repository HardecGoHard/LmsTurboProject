package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.LessonCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonCompletionRepository extends JpaRepository<LessonCompletion, Long> {

    Optional<LessonCompletion> findLessonCompletionByLessonIdAndUserId(Long lessonId, Long userId);
    List<LessonCompletion> findLessonCompletionByLessonId(Long lessonId);
    List<LessonCompletion> findLessonCompletionByUserId(Long userId);
}