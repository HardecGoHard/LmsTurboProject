package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.CourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<CourseDto> convertToDtoList(List<Course> courseList) {
        return courseList.stream().map(this::toCourseDto).collect(Collectors.toList());
    }

    public Page<CourseDto> convertToDtoList(Page<Course> courseList) {
        return new PageImpl<>(courseList.stream().map(this::toCourseDto).collect(Collectors.toList()),
                courseList.getPageable(), courseList.getTotalElements());
    }
}
