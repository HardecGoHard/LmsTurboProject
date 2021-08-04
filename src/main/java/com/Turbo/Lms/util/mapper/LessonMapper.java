package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.LessonDto;
import org.springframework.stereotype.Service;

@Service
public class LessonMapper {
    private final CourseRepository courseRepository;

    public LessonMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public LessonDto toLessonDto(Lesson lesson) {
        return new LessonDto(
                lesson.getId(),
                lesson.getText(),
                lesson.getCourse().getId()
        );
    }

    public Lesson toLessonFromDto(LessonDto lessonDto) {
        Lesson lesson = new Lesson(
                lessonDto.getId(),
                lessonDto.getTitle(),
                lessonDto.getText()
        );
        Course course = courseRepository.getById(lessonDto.getCourseId());
        lesson.setCourse(course);
        return lesson;
    }
}
