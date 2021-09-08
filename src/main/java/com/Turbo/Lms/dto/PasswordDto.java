package com.Turbo.Lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    private  String token;

    @Size(min = 8, message = "Длина пароля должна быть от 8 до 25")
    @NotBlank(message = "Пароль должен быть заполнен")
    private String newPassword;
    private String confirmNewPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordDto that = (PasswordDto) o;
        return Objects.equals(confirmNewPassword, that.confirmNewPassword) &&
                Objects.equals(token, that.token) &&
                Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmNewPassword, token, newPassword);
    }
}
