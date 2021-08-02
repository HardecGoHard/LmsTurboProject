package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.LessonRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.util.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService  {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Autowired
    private CourseService(CourseRepository courseRepository, CourseMapper courseMapper, LessonRepository lessonRepository,
                          UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    public CourseDto findById(long id) {
        return courseMapper.toCourseDto(courseRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Произошла ошибка: курс не найден ")));
    }

    public void save(CourseDto courseDto) {
        Course course = courseMapper.toCourseFromDto(courseDto);
        //Если это новый курс, то у него пока нет айди.
        if (course.getId() != null) {
            course.setLessons(lessonRepository.findLessonsByCourse_Id(course.getId()));
            course.setUsers(userRepository.getUsersOfCourse(course.getId()));
        }
        courseRepository.save(course);
    }
    public void delete(CourseDto courseDto) {
        courseRepository.delete(courseMapper.toCourseFromDto(courseDto));
    }

    public List<CourseDto> findByTitleLike(String prefix) {
        return courseRepository.findByTitleLike(prefix)
                .stream()
                .map(courseMapper::toCourseDto).
                        collect(Collectors.toList());
    }

    public List<CourseDto> findCoursesNotAssignToUser(Long userId, String title){
        return courseRepository.findCoursesByTitleNotAssignToUser(userId, title)
                .stream()
                .map(courseMapper::toCourseDto).collect(Collectors.toList());
    }

}

