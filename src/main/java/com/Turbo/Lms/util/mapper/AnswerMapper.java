package com.Turbo.Lms.util.mapper;

import com.Turbo.Lms.domain.Answer;
import com.Turbo.Lms.dto.AnswerDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class AnswerMapper {

    public AnswerDto toAnswerDto(Answer answer) {
        return new AnswerDto(
                answer.getId(),
                answer.getText(),
                answer.isCorrect(),
                answer.getTask().getId()
        );
    }

    public Answer toAnswerfromDto(AnswerDto answerDto) {
        return new Answer(
                answerDto.getId(),
                answerDto.getText(),
                answerDto.isCorrect()
        );
    }

    public List<AnswerDto> convertToDtoList(List<Answer> answerList) {
        return answerList.stream().map(this::toAnswerDto).collect(Collectors.toList());
    }
}
