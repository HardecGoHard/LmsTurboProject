package com.Turbo.Lms.dto;

import java.util.Objects;

public class PasswordDto {
    private  String token;

    private String newPassword;
    private String confirmNewPassword;

    public PasswordDto(String confirmNewPassword, String token, String newPassword) {
        this.confirmNewPassword = confirmNewPassword;
        this.token = token;
        this.newPassword = newPassword;
    }

    public PasswordDto() {
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

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
