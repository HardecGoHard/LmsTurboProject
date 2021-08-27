package com.Turbo.Lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
public class TaskDto {

    protected Long id;

    @NotEmpty(message = "Название должно быть заполнено")
    protected String name;

    @NotEmpty(message = "Вопрос должен быть заполнен")
    protected String question;

    @Min(value = 1, message = "Попытка должна быть как минимум одна")
    @Max(value = 3, message = "Не более 3 попыток, 4?? варианта ответа")
    protected short attempts;

    protected Long courseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return Objects.equals(id, taskDto.id) &&
                Objects.equals(name, taskDto.name) &&
                Objects.equals(question, taskDto.question) &&
                Objects.equals(courseId, taskDto.courseId) &&
                Objects.equals(attempts, taskDto.attempts);
    }

    public TaskDto() {}
    public TaskDto(Long courseId) {
        this.courseId = courseId;
    }
}
