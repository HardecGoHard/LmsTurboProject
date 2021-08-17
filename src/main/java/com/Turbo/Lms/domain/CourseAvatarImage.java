package com.Turbo.Lms.domain;

import javax.persistence.*;

@Entity
@Table(name = "course_avatar_images")
public class CourseAvatarImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contentType;

    @Column
    private String filename;

    @OneToOne
    private Course course;

    public CourseAvatarImage() {
    }

    public CourseAvatarImage(Long id, String contentType, String filename, Course course) {
        this.id = id;
        this.contentType = contentType;
        this.filename = filename;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
