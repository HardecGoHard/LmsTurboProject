package com.Turbo.Lms.service;


import com.Turbo.Lms.Exceptions.NotFoundException;
import com.Turbo.Lms.dao.AnswerRepository;
import com.Turbo.Lms.dao.CourseRepository;
import com.Turbo.Lms.dao.TaskRepository;
import com.Turbo.Lms.domain.Answer;
import com.Turbo.Lms.domain.Course;
import com.Turbo.Lms.domain.Task;
import com.Turbo.Lms.dto.TaskDto;
import com.Turbo.Lms.util.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AnswerRepository answerRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, AnswerRepository answerRepository, TaskMapper taskMapper,
                       CourseRepository courseRepository) {
        this.taskRepository = taskRepository;
        this.answerRepository = answerRepository;
        this.taskMapper = taskMapper;
        this.courseRepository = courseRepository;
    }

    public Long save(TaskDto taskDto) {
        Task task = taskMapper.toTaskfromDto(taskDto);
        Course course = courseRepository.getById(taskDto.getCourseId());
        task.setCourse(course);
        List<Answer> answerList = answerRepository.getAnswerByTaskId(task.getId());
        task.setAnswers(answerList);
        return taskRepository.save(task).getId();
    }

    public List<TaskDto> findByCourseId(@Param("courseId") Long courseId) {
        return taskMapper.convertToDtoList(taskRepository.findByCourseId(courseId));
    }

    public Task getById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Задание не найдено"));
    }

    public TaskDto findTaskById(Long taskId) {
       return taskRepository.findById(taskId).map(taskMapper::toTaskDtofromTask).orElseThrow(() -> new NotFoundException("Задание не найдено"));
    }

    public void delete(Long taskId) {
        taskRepository.delete(getById(taskId));
    }
}
