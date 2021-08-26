package com.Turbo.Lms.controller;

import com.Turbo.Lms.dto.LessonDto;
import com.Turbo.Lms.service.LessonCompletionService;
import com.Turbo.Lms.service.LessonService;
import com.Turbo.Lms.service.RoleType;
import com.Turbo.Lms.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc
public class LessonControllerTest {
    @MockBean
    private LessonService lessonService;
    @MockBean
    private LessonCompletionService lessonCompletionService;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final static LessonDto LESSON = new LessonDto(1L, "LESSON1", "TEXT1", 1L);

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void newLesson_Should_Return_True() throws Exception {
        mockMvc.perform(get("/lesson/new").param("courseId", LESSON.getCourseId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson_form"));
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void submitLessonForm_Should_Return_True() throws Exception {
        mockMvc.perform(post("/lesson").with(csrf())
                .flashAttr("lesson", LESSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/course/" + LESSON.getCourseId()));
        Mockito.verify(lessonService, Mockito.times(1)).save(LESSON);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void submitUserLesson_Should_Return_ValidErrors() throws Exception {
        LessonDto validErrorlessonDto = new LessonDto(1L, "", "", 2L);
        mockMvc.perform(post("/lesson").with(csrf())
                .flashAttr("lesson", validErrorlessonDto))
                .andExpect(model().hasErrors())
                .andExpect(view().name("lesson_form"));
        Mockito.verify(lessonService, Mockito.times(0)).save(validErrorlessonDto);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    @Disabled
    //TODO исправить тест
    void LessonForm_Should_Return_True() throws Exception {
        Mockito.when(lessonService.findById(LESSON.getId())).thenReturn(LESSON);
        this.mockMvc.perform(
                get("/lesson/{id}", LESSON.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson_form"))
                .andExpect(model().attribute("lesson", LESSON));
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void deleteLesson_Should_Return_True() throws Exception {
        Mockito.when(lessonService.findById(LESSON.getId())).thenReturn(LESSON);
        mockMvc.perform(delete("/lesson/{id}", LESSON.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course/" + LESSON.getCourseId()));
        Mockito.verify(lessonService, Mockito.times(1)).delete(LESSON.getId());
    }

}
