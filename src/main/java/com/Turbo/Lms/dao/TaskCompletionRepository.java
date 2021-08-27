package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.TaskCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskCompletionRepository extends JpaRepository<TaskCompletion,Long> {

    @Query("from TaskCompletion tc " +
            "where tc.id in (" +
            "select tc.id from TaskCompletion tc " +
            "left join tc.user u " +
            "where u.id = :userId)")
    List<TaskCompletion> getAllByUserId(@Param("userId") Long userId);

    @Query("from TaskCompletion tc " +
            "where tc.id in (" +
            "select tc.id from TaskCompletion tc " +
            "left join tc.task t " +
            "where t.id = :taskId)")
    List<TaskCompletion> getAllByTaskId(@Param("taskId") Long taskId);

    @Query("from TaskCompletion tc " +
            "where tc.id in (" +
            "select tc.id from TaskCompletion tc " +
            "left join tc.task t " +
            "where t.id = :taskId )" +
            "and tc.user.id in ( " +
            "select u.id from User u " +
            "where u.id = :userId)")
    Optional<TaskCompletion> getByUserIdAndTaskId(@Param("userId") Long userId, @Param("taskId")Long taskId);
}
