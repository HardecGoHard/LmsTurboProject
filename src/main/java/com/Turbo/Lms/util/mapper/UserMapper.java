package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    private final PasswordEncoder encoder;

    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public UserDto toUserDto(User user){
       return new UserDto(
               user.getId(),
               user.getUsername(),
               "",
               user.getEmail(),
               user.getRoles()
       );
    }
    public User toUserFromDto(UserDto userDto){
            return new User(
                    userDto.getId(),
                    userDto.getUsername(),
                    encoder.encode(userDto.getPassword()),
                    userDto.getEmail(),
                    userDto.getRoles()
            );
    }
    public List<UserDto> convertToDtoList(List<User> userList) {
        return userList.stream().map(this::toUserDto).collect(Collectors.toList());
    }
}
