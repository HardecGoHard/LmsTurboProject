package com.Turbo.Lms.domain;

import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String question;

    @Column
    private short attempts;

    @ManyToOne(optional = false)
    private Course course;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskCompletion> taskCompletions;

    public Task(Long id, String name, String question, short attempts) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.attempts = attempts;
    }
}
