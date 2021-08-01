package com.Turbo.Lms.dto;

import com.Turbo.Lms.domain.Role;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

public class UserDto {

    private Long id;
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String username;
    @NotBlank(message = "Пароль должен быть заполнен")
    private String password;

    private Set<Role> roles;


    public UserDto() {
    }

    public UserDto(Long id, String username, String password, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
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
}
