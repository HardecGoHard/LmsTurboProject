package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.LessonCompletionRepository;
import com.Turbo.Lms.domain.LessonCompletion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonCompletionService {
    private final LessonCompletionRepository lessonCompletionRepository;

    @Autowired
    LessonCompletionService(LessonCompletionRepository lessonCompletionRepository) {
        this.lessonCompletionRepository = lessonCompletionRepository;
    }

    public boolean isLessonAlreadyCompletedByUser(Long lessonId, Long userId) {
        return lessonCompletionRepository.findLessonCompletionByLessonIdAndUserId(lessonId, userId).isPresent();
    }

    public void save(LessonCompletion lessonCompletion) {
        lessonCompletionRepository.save(lessonCompletion);
    }

    public void deleteByLessonId(Long lessonId) {
       var list = lessonCompletionRepository.findLessonCompletionByLessonId(lessonId);
       for (var lesson : list) {
           lessonCompletionRepository.delete(lesson);
       }
    }

    public void deleteByUserId(Long userId) {
        var list = lessonCompletionRepository.findLessonCompletionByUserId(userId);
        for (var lesson : list) {
            lessonCompletionRepository.delete(lesson);
        }
    }
}
