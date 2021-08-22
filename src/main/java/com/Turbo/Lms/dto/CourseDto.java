package com.Turbo.Lms.dto;

import com.Turbo.Lms.annotations.TitleCase;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CourseDto {
    private Long id;
    @NotBlank(message = "Автор курса должен быть заполнен")
    private String author;

    @TitleCase
    @NotBlank(message = "Название курса должно быть заполнено")
    private String title;

    public CourseDto(Long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }
    public CourseDto(){

    }

    public CourseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
