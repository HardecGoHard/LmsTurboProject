package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.LessonCompletionRepository;
import com.Turbo.Lms.dao.LessonRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.LessonDto;
import com.Turbo.Lms.util.mapper.LessonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "lessonCache")
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final CourseRepository courseRepository;


    @Autowired
    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.courseRepository = courseRepository;
    }

    @Cacheable(cacheNames = "lesson", key = "#id", unless = "#result == null")
    public LessonDto findById(Long id) {
        Lesson lesson = findLessonById(id);
        return lessonMapper.toLessonDto(lesson);
    }

    @CacheEvict(cacheNames = "lessons", allEntries = true)
    public void save(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toLessonFromDto(lessonDto);
        Course course = courseRepository.findById((lessonDto.getCourseId())).get();
        lesson.setCourse(course);
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
