package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       CourseRepository courseRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {
        return convertListToDto(userRepository.findAll());
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public void save(UserDto userDto) {
        userRepository.save(userMapper.toUserFromDto(userDto));
    }

    public UserDto findById(long id) {
        return userRepository.findById(id)
                .map(usr -> userMapper.toUserDto(usr))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public List<UserDto> findUsersNotAssignedToCourse(long courseId) {
        return convertListToDto(userRepository.findUsersNotAssignedToCourse(courseId));
    }

    public List<UserDto> findByUsernameLike(String username) {
        return convertListToDto(userRepository.findByUsernameLike(username));
    }

    public List<UserDto> getUsersOfCourse(Long id) {
        return convertListToDto(new ArrayList<>(userRepository.getUsersOfCourse(id)));
    }

    public void assignUserById(Long id, Long courseId) {
        signUserToCourse(id, courseId, false);
    }

    public void unassignUserFromCourseById(Long id, Long courseId) {
        signUserToCourse(id, courseId, true);
    }

    private List<UserDto> convertListToDto(List<User> list) {
        return list.stream().map(u -> userMapper.toUserDto(u)).collect(Collectors.toList());
    }

    private void signUserToCourse(Long id, Long courseId, boolean isDelete) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Course course =  courseRepository.findById(courseId).get();
        if (isDelete) {
            user.getCourses().remove(course);
            course.getUsers().remove(user);
        } else {
            course.getUsers().add(user);
            user.getCourses().add(course);
        }
        courseRepository.save(course);
    }

    public UserDto findUserByUsername(String remoteUser) {
        return userRepository.findUserByUsername(remoteUser).map(usr -> userMapper.toUserDto(usr))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
