package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.UserAvatarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAvatarImageRepository extends JpaRepository<UserAvatarImage, Long> {
    Optional<UserAvatarImage> findAvatarImageByUser_Id (Long user_id);
    Optional<UserAvatarImage> findAvatarImageByUser_Username(String username);
}
