package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.LessonRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.LessonDto;
import com.Turbo.Lms.util.mapper.CourseMapper;
import com.Turbo.Lms.util.mapper.LessonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;


    @Autowired
    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    public LessonDto findById(Long id) {
        Lesson lesson = findLessonById(id);
        return lessonMapper.toLessonDto(lesson);
    }

    public void save(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toLessonFromDto(lessonDto);
        lessonRepository.save(lesson);
    }

    public List<LessonDto> findAllForLessonIdWithoutText(@Param("id") long courseId) {
        return lessonRepository.findAllForLessonIdWithoutText(courseId);
    }

    public void delete(Long id) {
        lessonRepository.delete(findLessonById(id));
    }

    private Lesson findLessonById(Long id) {
        return lessonRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Произошла ошибка: урок не найден")
        );
    }
}
