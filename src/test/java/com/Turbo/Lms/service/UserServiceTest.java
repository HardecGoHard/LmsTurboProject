package com.Turbo.Lms.service;

import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.PasswordResetTokenRepository;
import com.Turbo.Lms.dao.RoleRepositorty;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.util.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;


public class UserServiceTest {

    private static UserService service;
    private static UserMapper userMapper;

    private static UserRepository userRepositoryMock;
    private static PasswordEncoder passwordEncoderMock;
    private static RoleRepositorty roleRepositortyMock;
    private static PasswordResetTokenRepository passwordResetTokenMock;

    private static final Role ROLE_ADMIN = new Role("ROLE_ADMIN");
    private static final Role ROLE_STUDENT = new Role("ROLE_STUDENT");
    private static List<User> userList;


    private static final Long INCORRECT_USER_ID = 1234567890L;

    @BeforeAll
    public static void setUp() {
        userList = List.of(
                new User(
                        1L,
                        "NAME1",
                        "hash",
                        "email@gmail.com",
                        Set.of(ROLE_ADMIN)),
                new User(
                        2L,
                        "NAME2",
                        "hash",
                        "email2@gmail.com",
                        Set.of(ROLE_STUDENT))
        );

        passwordEncoderMock = Mockito.mock(PasswordEncoder.class);
        userRepositoryMock = Mockito.mock(UserRepository.class);
        roleRepositortyMock = Mockito.mock(RoleRepositorty.class);
        passwordResetTokenMock = Mockito.mock(PasswordResetTokenRepository.class);
        Mockito.when(passwordEncoderMock.encode(anyString())).thenReturn("hash");

        userMapper = new UserMapper(passwordEncoderMock);

        service = new UserService(userRepositoryMock, userMapper, roleRepositortyMock, passwordEncoderMock, passwordResetTokenMock);
    }

    @Test
    public void findAll_Should_Return_True() {
        Mockito.when(userRepositoryMock.findAll()).thenReturn(userList);

        List<UserDto> allUser = userMapper.convertToDtoList(userRepositoryMock.findAll());
        assertThat(service.findAll()).isEqualTo(allUser);
    }

    @Test
    public void findAll_Should_Return_False() {
        Mockito.when(userRepositoryMock.findAll()).thenReturn(userList);
        List<UserDto> allUser = userMapper.convertToDtoList(userRepositoryMock.findAll());
        allUser.get(0).setUsername("weqeqweqeqee");

        assertThat(service.findAll().equals(allUser)).isFalse();
    }

    @Test
    public void findById_Should_Return_True() {
        Mockito.when(userRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(userList.get(0)));
        UserDto user = userMapper.toUserDto(userRepositoryMock.findById(1L).get());

        assertThat(service.findById(1L)).isEqualTo(user);
    }

    @Test
    public void findById_Should_Return_NotFoundException() {
        Mockito.when(userRepositoryMock.findById(INCORRECT_USER_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            service.findById(INCORRECT_USER_ID);
        });

        Mockito.when(userRepositoryMock.findById(null)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> {
            service.findById(null);
        });
    }


    @Test
    public void delete_Should_Return_True() {
        Mockito.when(userRepositoryMock.findById(userList.get(0).getId()))
                .thenReturn(Optional.ofNullable(userList.get(1)));
        service.delete(userMapper.toUserDto(userList.get(0)));
        Mockito.verify(userRepositoryMock, Mockito.times(1)).delete(userList.get(1));
    }

    @Test
    public void saveUser_Should_Return_NullPointerException() {
        Mockito.when(userRepositoryMock.save(null)).thenThrow(NullPointerException.class);
        Assertions.assertThrows(NullPointerException.class, () -> {
            service.save(null);
        });
    }

    @Test
    public void save_Should_Return_True() {
        User newUser = new User(3L,
                "NAME3",
                "hash",
                "email@gmail.com",
                Set.of(ROLE_STUDENT));
        service.save(userMapper.toUserDto(newUser));
        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(newUser);
    }

}
