package com.Turbo.Lms.dto;

import com.Turbo.Lms.annotations.TitleCase;
import com.Turbo.Lms.domain.Lesson;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class CourseDto {
    private Long id;

    private String author;

    private String title;

    public CourseDto(Long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
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


}
