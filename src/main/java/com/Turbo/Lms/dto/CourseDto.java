package com.Turbo.Lms.dto;

import com.Turbo.Lms.annotations.TitleCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Long id;
    @NotBlank(message = "Автор курса должен быть заполнен")
    private String author;

    @TitleCase
    @NotBlank(message = "Название курса должно быть заполнено")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDto courseDto = (CourseDto) o;
        return Objects.equals(id, courseDto.id) &&
                Objects.equals(author, courseDto.author) &&
                Objects.equals(title, courseDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title);
    }
}
