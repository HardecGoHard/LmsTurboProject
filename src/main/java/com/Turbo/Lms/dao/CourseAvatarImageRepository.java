package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.CourseAvatarImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseAvatarImageRepository extends JpaRepository<CourseAvatarImage, Long> {
    Optional<CourseAvatarImage> findByCourse_Id(Long courseId);
}
