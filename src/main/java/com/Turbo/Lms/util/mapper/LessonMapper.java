package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Lesson;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.dto.LessonDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonMapper {


    public LessonDto toLessonDto(Lesson lesson) {
        return new LessonDto(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getText(),
                lesson.getCourse().getId()
        );
    }

    public Lesson toLessonFromDto(LessonDto lessonDto) {
        return new Lesson(
                lessonDto.getId(),
                lessonDto.getTitle(),
                lessonDto.getText()
        );
    }

    public List<LessonDto> convertToDtoList(List<Lesson> courseList) {
        return courseList.stream().map(this::toLessonDto).collect(Collectors.toList());
    }
}
