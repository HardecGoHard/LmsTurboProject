package com.Turbo.Lms.service;


import com.Turbo.Lms.dao.AnswerRepository;
import com.Turbo.Lms.dao.TaskRepository;
import com.Turbo.Lms.domain.Answer;
import com.Turbo.Lms.dto.AnswerDto;
import com.Turbo.Lms.util.mapper.AnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository, AnswerMapper answerMapper, TaskRepository taskRepository) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
        this.taskRepository = taskRepository;
    }

    public List<AnswerDto> findByTaskId(Long taskId) {
        return answerMapper.convertToDtoList(answerRepository.getAnswerByTaskId(taskId));
    }

    public Answer getById(Long answerId) {
        return answerRepository.getById(answerId);
    }

    public void save(AnswerDto answerDto) {
        Answer answer = answerMapper.toAnswerfromDto(answerDto);
        answer.setTask(taskRepository.getById(answerDto.getTaskId()));
        answerRepository.save(answer);
    }
}
