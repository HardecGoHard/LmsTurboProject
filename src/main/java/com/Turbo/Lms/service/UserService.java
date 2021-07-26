package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserRepository userRepository;
    CourseService courseService;

    @Autowired
    public UserService(UserRepository userRepository, CourseService courseService) {
        this.userRepository = userRepository;
        this.courseService = courseService;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );
    }

    public List<UserDto> findUsersNotAssignedToCourse(long courseId) {
        return convertListToDto(userRepository.findUsersNotAssignedToCourse(courseId));
    }

    public List<UserDto> getUsersOfCourse(Long id) {
        return convertListToDto(userRepository.getUsersOfCourse(id));
    }

    public void assignUserById(Long id, Long courseId) {
        signUser(id, courseId, false);
    }

    public void unassignUserById(Long id, Long courseId) {
        signUser(id, courseId, true);
    }

    private List<UserDto> convertListToDto(List<User> list) {
        return list.stream().map(u -> new UserDto(
                u.getId(),
                u.getUsername())).collect(Collectors.toList());
    }

    private void signUser(Long id, Long courseId, boolean isDelete) {
        User user = findById(id);
        Course course = courseService.findById(courseId);
        if (isDelete) {
            user.getCourses().remove(course);
            course.getUsers().remove(user);
        } else {
            course.getUsers().add(user);
            user.getCourses().add(course);
        }
        courseService.save(course);
    }
}
