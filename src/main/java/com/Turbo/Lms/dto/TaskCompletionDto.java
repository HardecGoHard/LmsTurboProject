package com.Turbo.Lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCompletionDto {
    private Long id;

    private Integer attemptsNumber;

    private boolean completed;

    private Long userId;

    private Long taskId;

    public TaskCompletionDto(Long userId, Long taskId, Integer attemptsNumber, boolean completed) {
        this.userId = userId;
        this.taskId = taskId;
        this.attemptsNumber = attemptsNumber;
        this.completed = completed;
    }
}
