package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.AvatarImage;
import com.Turbo.Lms.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarImageRepository extends JpaRepository<AvatarImage, Long> {
    Optional<AvatarImage> findAvatarImageByUser_Id (Long user_id);
    Optional<AvatarImage> findAvatarImageByUser_Username(String username);
}
