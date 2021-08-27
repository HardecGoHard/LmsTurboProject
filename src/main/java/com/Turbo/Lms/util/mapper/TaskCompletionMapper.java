package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.domain.Task;
import com.Turbo.Lms.domain.TaskCompletion;
import com.Turbo.Lms.dto.TaskCompletionDto;
import com.Turbo.Lms.dto.TaskDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskCompletionMapper {

    public TaskCompletion fromDto(TaskCompletionDto taskCompletionDto) {
        return new TaskCompletion(
                taskCompletionDto.getId(),
                taskCompletionDto.isCompleted(),
                taskCompletionDto.getAttemptsNumber()
        );
    }

    public TaskCompletionDto toDto(TaskCompletion taskCompletion) {
        return new TaskCompletionDto(
                taskCompletion.getId(),
                taskCompletion.getAttemptsNumber(),
                taskCompletion.isCompleted(),
                taskCompletion.getUser().getId(),
                taskCompletion.getTask().getId()
        );
    }

    public List<TaskCompletionDto> convertToDtoList(List<TaskCompletion> taskList) {
        return taskList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
