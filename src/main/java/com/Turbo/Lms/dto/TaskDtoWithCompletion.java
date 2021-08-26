package com.Turbo.Lms.dto;

public class TaskDtoWithCompletion extends  TaskDto{
    private boolean completion;

    public TaskDtoWithCompletion(TaskDto taskDto, boolean completion) {
        this.id = taskDto.id;
        this.attempts = taskDto.attempts;
        this.courseId = taskDto.courseId;
        this.name = taskDto.name;
        this.question = taskDto.question;
        this.completion = completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    public boolean getCompletion() {
        return completion;
    }
}
