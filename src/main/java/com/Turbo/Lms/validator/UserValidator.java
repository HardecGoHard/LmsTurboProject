package com.Turbo.Lms.validator;

import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDto currentUser = (UserDto) o;
        UserDto userByUsername= userService.findUserByUsername(currentUser.getUsername());
        UserDto userByEmail = userService.findUserByEmail(currentUser.getEmail());

        if (userByUsername != null && !userByUsername.getId().equals(currentUser.getId())){
            errors.reject("username", "Пользователь с таким username уже существует");
        }
        if (userService.findUserByEmail(currentUser.getEmail()) != null
                && !userByEmail.getId().equals(currentUser.getId())){
            errors.reject("email", "Пользователь с таким email уже существует");
        }
        if (!currentUser.getPassword().equals(currentUser.getConfirmPassword())){
            errors.reject("confirmPassword", "Пароли не совпадают");
        }
    }
}
