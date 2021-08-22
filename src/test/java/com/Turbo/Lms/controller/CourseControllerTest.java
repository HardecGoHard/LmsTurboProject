package com.Turbo.Lms.controller;

import com.Turbo.Lms.dto.CourseDto;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc
public class CourseControllerTest {
    @MockBean
    private CourseService courseService;
    @MockBean
    private LessonService lessonService;
    @MockBean
    private UserService userService;
    @MockBean
    private CourseAvatarStorageService courseAvatarStorageServiceMock;


    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void courseTable_Admin_Should_Return_True() throws Exception {
        List<CourseDto> courseDtoList = List.of(new CourseDto(1L, "NAME1", "TITLE1"));
        Mockito.when(courseService.findByTitleLike("%")).thenReturn(courseDtoList);
        this.mockMvc.perform(
                get("/course"))
                .andExpect(status().isOk())
                .andExpect(view().name("find_course"))
                .andExpect(model().attribute("courses", courseDtoList));
    }

    @WithMockUser(username = "user", roles = RoleType.STUDENT_WITHOUT_PREFIX)
    @Test
    void courseTable_Student_Should_Return_True() throws Exception {
        List<CourseDto> courseDtoList = List.of(new CourseDto(1L, "NAME1", "TITLE1"));
        Mockito.when(userService.findUserByUsername("user")).thenReturn(new UserDto(1L));
        Mockito.when(courseService.findCoursesNotAssignToUser(1L, "%")).thenReturn(courseDtoList);
        this.mockMvc.perform(
                get("/course"))
                .andExpect(status().isOk())
                .andExpect(view().name("find_course"))
                .andExpect(model().attribute("courses", courseDtoList));
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void courseForm_Should_Return_True() throws Exception {
        CourseDto courseDto = new CourseDto(1L);
        Mockito.when(courseService.findById(courseDto.getId())).thenReturn(courseDto);
        Mockito.when(lessonService.findAllForLessonIdWithoutText(1L)).thenReturn(Collections.emptyList());
        Mockito.when(userService.getUsersOfCourse(1L)).thenReturn(Collections.emptyList());
        this.mockMvc.perform(
                get("/course/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("form_course"))
                .andExpect(model().attribute("course", courseDto))
                .andExpect(model().attribute("lessons", Collections.emptyList()))
                .andExpect(model().attribute("users", Collections.emptyList()));

    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void submitCourseForm_Should_Return_True() throws Exception {
        CourseDto courseDto = new CourseDto(1L, "NAMEONE", "COURSE ONE");

        mockMvc.perform(post("/course").with(csrf())
                .flashAttr("course", courseDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/course"));

        Mockito.verify(courseService, Mockito.times(1)).save(courseDto);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void submitCourseForm_Should_Return_ValidErrors() throws Exception {
        CourseDto courseDto = new CourseDto(1L, "", "");

        mockMvc.perform(post("/course").with(csrf())
                .flashAttr("course", courseDto))
                .andExpect(model().hasErrors())
                .andExpect(view().name("form_course"));


        Mockito.verify(courseService, Mockito.times(0)).save(courseDto);
    }

    @WithMockUser(username = "user", roles = RoleType.STUDENT_WITHOUT_PREFIX)
    @Test
    void submitCourseForm_Should_Return_AccessDenied() throws Exception {
        CourseDto courseDto = new CourseDto(1L, "NAMEONE", "COURSE ONE");

        mockMvc.perform(post("/course")
                .flashAttr("course", courseDto))
                .andExpect(status().isForbidden());
        Mockito.verify(courseService, Mockito.times(0)).save(courseDto);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void newCourse_Should_Return_True() throws Exception {
        mockMvc.perform(get("/course/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form_course"));
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void deleteCourse_Should_Return_True() throws Exception {
        CourseDto courseDto = new CourseDto(1L);
        Mockito.when(courseService.findById(courseDto.getId())).thenReturn(courseDto);
        mockMvc.perform(delete("/course/{id}", 1L)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"));
        Mockito.verify(courseService, Mockito.times(1)).delete(courseDto);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void usersAssignForm_Should_Return_True() throws Exception {
        Long testCourseId = 2L;

        List<UserDto> userDtoList = List.of(new UserDto(1L));

        Mockito.when(userService.findUsersNotAssignedToCourse(testCourseId)).thenReturn(userDtoList);
        mockMvc.perform(get("/course/{courseId}/assign", testCourseId))
                .andExpect(status().isOk())
                .andExpect(view().name("assign_user"))
                .andExpect(model().attribute("users", userDtoList))
                .andExpect(model().attribute("courseId", testCourseId));
    }

    @WithMockUser(username = "user", roles = RoleType.STUDENT_WITHOUT_PREFIX)
    @Test
    void assignUser_Student_Should_Return_True() throws Exception {
        Long testCourseId = 2L;
        UserDto userDto = new UserDto(1L);
        mockMvc.perform(post("/course/{courseId}/assign", testCourseId)
                .with(csrf()).param("userId", userDto.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"));

        Mockito.verify(courseService, Mockito.times(1))
                .assignUserById(userDto.getId(), testCourseId);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void assignUser_Admin_Should_Return_True() throws Exception {
        Long testCourseId = 2L;
        UserDto userDto = new UserDto(1L);
        mockMvc.perform(post("/course/{courseId}/assign", testCourseId)
                .with(csrf()).param("userId", userDto.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course/" + testCourseId + "/assign"));

        Mockito.verify(courseService, Mockito.times(1))
                .assignUserById(userDto.getId(), testCourseId);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void unassignUser_Should_Return_True() throws Exception {
        Long testCourseId = 2L;
        UserDto userDto = new UserDto(1L);
        mockMvc.perform(delete("/course/{courseId}/unsign", testCourseId)
                .with(csrf()).param("userId", userDto.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course/" + testCourseId));

        Mockito.verify(courseService, Mockito.times(1))
                .unassignUserFromCourseById(userDto.getId(), testCourseId);
    }
}
