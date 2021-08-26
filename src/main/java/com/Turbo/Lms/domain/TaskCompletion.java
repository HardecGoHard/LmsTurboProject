package com.Turbo.Lms.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "completed_tasks")
public class TaskCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean completed;

    @Column
    private Integer attemptsNumber;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Task task;

    public TaskCompletion(Long id, boolean completed, Integer attemptsNumber) {
        this.id = id;
        this.completed = completed;
        this.attemptsNumber = attemptsNumber;
    }

    public TaskCompletion(boolean completed, Integer attemptsNumber) {
        this.completed = completed;
        this.attemptsNumber = attemptsNumber;
    }

}
