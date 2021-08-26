package com.Turbo.Lms.dto;

import com.Turbo.Lms.domain.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

public class UserDto {

    private Long id;
    @Size(min = 4, max = 25, message = "Длина должна быть от 4 до 25")
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String username;
    @Size(min = 8, message = "Длина пароля должна быть от 4 до 25")
    @NotBlank(message = "Пароль должен быть заполнен")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "email должен быть заполнен")
    @Email
    private String email;

    private Set<Role> roles;


    public UserDto() {
    }

    public UserDto(Long id, String username, String password, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }


    public UserDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCourseId(Long courseId) {
        courseId = courseId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) &&
                Objects.equals(username, userDto.username) &&
                Objects.equals(password, userDto.password) &&
                Objects.equals(roles, userDto.roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, roles);
    }
}
