package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.domain.Task;
import com.Turbo.Lms.dto.TaskDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public Task toTaskfromDto(TaskDto taskDto) {
        return new Task (
                taskDto.getId(),
                taskDto.getName(),
                taskDto.getQuestion(),
                taskDto.getAttempts()
        );
    }

    public TaskDto toTaskDtofromTask(Task task) {
        return new TaskDto (
                task.getId(),
                task.getName(),
                task.getQuestion(),
                task.getAttempts(),
                task.getCourse().getId()
        );
    }

    public List<TaskDto> convertToDtoList(List<Task> taskList) {
        return taskList.stream().map(this::toTaskDtofromTask).collect(Collectors.toList());
    }
}
