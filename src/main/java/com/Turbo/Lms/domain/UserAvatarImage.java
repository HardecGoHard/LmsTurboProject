package com.Turbo.Lms.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_avatar_images")
public class UserAvatarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contentType;

    @Column
    private String filename;

    @OneToOne
    private User user;

    public UserAvatarImage(Long id, String contentType, String filename, User user) {
        this.id = id;
        this.contentType = contentType;
        this.filename = filename;
        this.user = user;
    }

    public UserAvatarImage() {

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAvatarImage that = (UserAvatarImage) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, filename);
    }
}