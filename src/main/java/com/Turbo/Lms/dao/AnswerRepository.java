package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.Answer;
import com.Turbo.Lms.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {

    @Query("from Answer a " +
            "where a.id in ( " +
            "select a.id from Answer a" +
            "left join a.task t " +
            "where t.id = :taskId )")
    List<Answer> getAnswerByTaskId(@Param("taskId") Long taskId);
}
