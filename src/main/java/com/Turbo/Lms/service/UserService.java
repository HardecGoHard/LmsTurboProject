package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {
        return userMapper.convertToDtoList(userRepository.findAll());
    }

    public void delete(UserDto userDto) {
        userRepository.delete(userMapper.toUserFromDto(userDto));
    }

    public void save(UserDto userDto) {
        userRepository.save(userMapper.toUserFromDto(userDto));
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public List<UserDto> findUsersNotAssignedToCourse(Long courseId) {
        return userMapper.convertToDtoList(userRepository.findUsersNotAssignedToCourse(courseId));
    }

    public List<UserDto> findByUsernameLike(String username) {
        return userMapper.convertToDtoList(userRepository.findByUsernameLike(username));
    }

    public List<UserDto> getUsersOfCourse(Long id) {
        return  userMapper.convertToDtoList(new ArrayList<>(userRepository.getUsersOfCourse(id)));
    }

    public UserDto findUserByUsername(String remoteUser) {
        return userRepository.findUserByUsername(remoteUser).map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
