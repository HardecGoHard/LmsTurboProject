package com.Turbo.Lms.dto;

public class LessonDtoWithCompletion extends LessonDto {

    private boolean completion = false;

    public LessonDtoWithCompletion(LessonDto lessonDto, boolean completion) {
        this.id = lessonDto.getId();
        this.courseId = lessonDto.getCourseId();
        this.text = lessonDto.getText();
        this.title = lessonDto.getTitle();
        this.completion = completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    public boolean getCompletion() {
        return completion;
    }
}
