package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.RoleRepositorty;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepositorty roleRepositorty;

    @Autowired
    public UserService(UserRepository userRepository, CourseService courseService, PasswordEncoder encoder, RoleRepositorty roleRepositorty) {
        this.userRepository = userRepository;
        this.courseService = courseService;
        this.encoder = encoder;
        this.roleRepositorty = roleRepositorty;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(usr -> new UserDto(usr.getId(), usr.getUsername(), "", usr.getRoles()))
                .collect(Collectors.toList());
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public void save(UserDto userDto) {
        userRepository.save(new User(userDto.getId(),
                userDto.getUsername(),
                encoder.encode(userDto.getPassword()),
                        userDto.getRoles()
        ));
    }

    public UserDto findById(long id) {
        return userRepository.findById(id)
                .map(usr -> new UserDto(usr.getId(), usr.getUsername(), "", usr.getRoles()))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public List<UserDto> findUsersNotAssignedToCourse(long courseId) {
        return convertListToDto(userRepository.findUsersNotAssignedToCourse(courseId));
    }

    public List<UserDto> findByUsernameLike(String username) {
        return convertListToDto(userRepository.findByUsernameLike(username));
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
                u.getUsername(), u.getPassword(), u.getRoles())).collect(Collectors.toList());
    }

    @ModelAttribute("roles")
    public List<Role> rolesAttribute() {
        return roleRepositorty.findAll();
    }

    private void signUser(Long id, Long courseId, boolean isDelete) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
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

    public UserDto findUserByUsername(String remoteUser) {
        return userRepository.findUserByUsername(remoteUser).map(usr -> new UserDto(usr.getId(), usr.getUsername(), "", usr.getRoles()))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
