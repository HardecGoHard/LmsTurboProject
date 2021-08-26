package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.PasswordResetToken;
import com.Turbo.Lms.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser_Id(Long userId);
    void deleteByUser_Id(Long userId);
}
