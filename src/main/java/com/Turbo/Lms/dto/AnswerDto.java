package com.Turbo.Lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private Long id;

    private String text;

    private boolean correct;

    private Long taskId;


    /*public AnswerDto(Long id, String text, boolean correct, Long taskId) {
        this.id = id;
        this.text = text;
        this.correct = correct;
        this.taskId = taskId;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerDto answerDto = (AnswerDto) o;
        return Objects.equals(id, answerDto.id) &&
                Objects.equals(text, answerDto.text) &&
                Objects.equals(correct, answerDto.correct) &&
                Objects.equals(taskId, answerDto.taskId);
    }

}
