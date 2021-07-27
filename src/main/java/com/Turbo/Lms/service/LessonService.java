package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.LessonRepository;
import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.LessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    public LessonRepository lessonRepository;

    public CourseService courseService;

    @Autowired
    public LessonService(LessonRepository lessonRepository, CourseService courseService) {
        this.lessonRepository = lessonRepository;
        this.courseService = courseService;
    }

    public LessonDto findById(Long id) {
        Lesson lesson = findLessonById(id);
        return new LessonDto(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getText(),
                lesson.getCourse().getId());
    }

    public void save(LessonDto lessonDto) {
        Lesson lesson = new Lesson(lessonDto.getId(), lessonDto.getTitle(), lessonDto.getText());
        lesson.setCourse(courseService.findById(lessonDto.getCourseId()));
        lessonRepository.save(lesson);
    }

    public List<LessonDto> findAllForLessonIdWithoutText(@Param("id") long id) {
        return lessonRepository.findAllForLessonIdWithoutText(id);
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
