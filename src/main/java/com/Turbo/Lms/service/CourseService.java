package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.LessonRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.util.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, LessonRepository lessonRepository,
                         UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    public CourseDto findById(long id) {
        return courseMapper.toCourseDto(courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Произошла ошибка: курс не найден ")));
    }

    public List<CourseDto> findAll() {
        return courseMapper.convertToDtoList(courseRepository.findAll());
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
        courseRepository.delete(courseRepository.findById(courseDto.getId()).orElseThrow(() -> new NotFoundException("Course not found!")));
    }

    public Page<CourseDto> findByTitleLike(String prefix, Pageable pageable) {
        return courseMapper.convertToDtoList(courseRepository.findByTitleLike(prefix, pageable));
    }

    public Page<CourseDto> findCoursesNotAssignToUser(Long userId, String title, Pageable pageable) {
        return courseMapper.convertToDtoList(courseRepository.findCoursesByTitleNotAssignToUser(userId, title, pageable));
    }

    public void assignUserById(Long id, Long courseId) {
        userToCourse(id, courseId, false);
    }

    public void unassignUserFromCourseById(Long id, Long courseId) {
        userToCourse(id, courseId, true);
    }

    private void userToCourse(Long id, Long courseId, boolean isDelete) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Course course = courseRepository.findById(courseId).get();
        if (isDelete) {
            user.getCourses().remove(course);
            course.getUsers().remove(user);
        } else {
            course.getUsers().add(user);
            user.getCourses().add(course);
        }
        courseRepository.save(course);
    }
}

