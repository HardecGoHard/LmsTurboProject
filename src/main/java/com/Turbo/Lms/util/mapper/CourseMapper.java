package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.CourseDto;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDto toCourseDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getAuthor(),
                course.getTitle()
        );
    }
    public Course toCourseFromDto(CourseDto courseDto) {
        return new Course(
                courseDto.getId(),
                courseDto.getAuthor(),
                courseDto.getTitle()
        );
    }
}
