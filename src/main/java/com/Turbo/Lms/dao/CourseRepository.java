package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.dto.CourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByTitleLike(String title, Pageable pageable);

    @Query("select new com.Turbo.Lms.dto.CourseDto(c.id, c.author, c.title) " +
            "from Course c where c.id = :id")
    CourseDto findByIdAndConvertToDto(@Param("id") long id);

    //Вывод курсов, на которые пользователь ещё не записался
    @Query("from Course c " +
            "where c.id not in ( " +
            "select c.id " +
            "from Course c " +
            "left join c.users u " +
            "where u.id = :userId) and c.title like :title")
    Page<Course> findCoursesByTitleNotAssignToUser(@Param("userId") long userId, @Param("title") String title,
                                                   Pageable pageable);

    @Query("from Course c " +
            "where c.id in ( " +
            "select c.id " +
            "from Course c " +
            "left join c.users u " +
            "where u.id = :userId)")
    Page<Course> findCoursesAssignedToUser(@Param("userId") long userId, Pageable pageable);
}