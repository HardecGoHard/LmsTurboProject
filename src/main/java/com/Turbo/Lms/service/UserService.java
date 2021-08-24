package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.RoleRepositorty;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepositorty roleRepositorty;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper,RoleRepositorty roleRepositorty) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepositorty = roleRepositorty;
    }

    public List<UserDto> findAll() {
        return userMapper.convertToDtoList(userRepository.findAll());
    }

    public void delete(UserDto userDto) {
        userRepository.delete(userRepository.findById(userDto.getId()).orElseThrow(() -> new NotFoundException("User not found!")));
    }

    public void save(UserDto userDto) {
        userRepository.save(userMapper.toUserFromDto(userDto));
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public boolean isEnrolled(Long userId, Long courseId){
        Optional<User> user = userRepository.findIfUserIsEnrolledOnCourse(userId, courseId);
        return user.isPresent();
    }


    public List<UserDto> findUsersNotAssignedToCourse(Long courseId) {
        return userMapper.convertToDtoList(userRepository.findUsersNotAssignedToCourse(courseId));
    }

    public List<UserDto> findByUsernameLike(String username) {
        return userMapper.convertToDtoList(userRepository.findByUsernameLike(username));
    }

    public List<UserDto> getUsersOfCourse(Long id) {
        return userMapper.convertToDtoList(new ArrayList<>(userRepository.getUsersOfCourse(id)));
    }

    public UserDto findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(userMapper::toUserDto).orElse(null);
    }

    public void registerUser(UserDto userDto) {
        Set<Role> roles = Set.of(roleRepositorty.findRoleByName(RoleType.STUDENT).get());
        userDto.setRoles(roles);
        save(userDto);
    }

    public UserDto findUserByUsername(String remoteUser) {
        Optional<User> user = userRepository.findUserByUsername(remoteUser);
        return user.map(userMapper::toUserDto).orElse(null);
    }
}
