package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("from Task t " +
            "where t.id in ( " +
            "select t.id from Task t " +
            "left join t.course c " +
            "where c.id = :courseId)")
    List<Task> findByCourseId(@Param("courseId") Long courseId);
}
