package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.LessonDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("from User u " +
            "where u.id not in ( " +
            "select u.id " +
            "from User u " +
            "left join u.courses c " +
            "where c.id = :courseId)")
    List<User> findUsersNotAssignedToCourse(@Param("courseId") long courseId);

    @Query("from User u " +
            "left join u.courses c " +
            "where c.id = :courseId")
    Set<User> getUsersOfCourse(@Param("courseId") long courseId);

    Optional<User> findUserByUsername(String username);
    List<User> findByUsernameLike(String username);
}
