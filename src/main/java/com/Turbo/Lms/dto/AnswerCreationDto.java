package com.Turbo.Lms.dto;

import com.Turbo.Lms.domain.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnswerCreationDto {
    private List<AnswerDto> answerDtoList;

    public AnswerCreationDto(List<AnswerDto> answerDtoList) {
        this.answerDtoList = answerDtoList;
    }
}
