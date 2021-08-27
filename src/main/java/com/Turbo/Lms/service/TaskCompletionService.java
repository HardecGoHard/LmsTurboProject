package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.TaskCompletionRepository;
import com.Turbo.Lms.dao.TaskRepository;
import com.Turbo.Lms.dao.UserRepository;
import com.Turbo.Lms.domain.Task;
import com.Turbo.Lms.domain.TaskCompletion;
import com.Turbo.Lms.domain.User;
import com.Turbo.Lms.dto.TaskCompletionDto;
import com.Turbo.Lms.util.mapper.TaskCompletionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskCompletionService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskCompletionRepository taskCompletionRepository;
    private final TaskCompletionMapper taskCompletionMapper;

    @Autowired
    public TaskCompletionService(TaskCompletionRepository taskCompletionRepository, TaskCompletionMapper taskCompletionMapper,
                                 UserRepository userRepository, TaskRepository taskRepository) {
        this.taskCompletionRepository = taskCompletionRepository;
        this.taskCompletionMapper = taskCompletionMapper;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public TaskCompletionDto getById(Long id) {
        return taskCompletionMapper.toDto(taskCompletionRepository.getById(id));
    }

    public Optional<TaskCompletionDto> findByUserAndTaskId(Long userId, Long taskId) {
        var result = taskCompletionRepository.getByUserIdAndTaskId(userId, taskId);
        return result.map(taskCompletionMapper::toDto);
    }

    public List<TaskCompletionDto> getAllByUserId(Long userId) {
        return taskCompletionMapper.convertToDtoList(taskCompletionRepository.getAllByUserId(userId));
    }

    public List<TaskCompletionDto> getAllByTaskId(Long taskId) {
        return taskCompletionMapper.convertToDtoList(taskCompletionRepository.getAllByTaskId(taskId));
    }

    public boolean isTaskAlreadyCompletedByUser(Long userId, Long taskId){
        var record = taskCompletionRepository.getByUserIdAndTaskId(userId, taskId);
        return (record.isPresent() && record.get().isCompleted());
    }

    public void save(TaskCompletionDto taskCompletionDto) {
        TaskCompletion taskCompletion = taskCompletionMapper.fromDto(taskCompletionDto);
        User user = userRepository.findById(taskCompletionDto.getUserId()).get();
        Task task = taskRepository.findById(taskCompletionDto.getTaskId()).get();
        taskCompletion.setTask(task);
        taskCompletion.setUser(user);
        taskCompletionRepository.save(taskCompletion);
    }

}
