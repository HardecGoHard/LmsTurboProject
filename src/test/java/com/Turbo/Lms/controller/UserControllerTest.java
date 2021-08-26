package com.Turbo.Lms.controller;

import com.Turbo.Lms.domain.Role;
import com.Turbo.Lms.dto.UserDto;
import com.Turbo.Lms.service.*;
import com.Turbo.Lms.validator.UserValidator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private RoleService roleService;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private CourseService courseService;
    @MockBean
    private LessonCompletionService lessonCompletionService;


    @Autowired
    private MockMvc mockMvc;

    private static final List<Role> ROLE_LIST = List.of(
            new Role("ROLE_STUDENT"),
            new Role("ROLE_ADMIN")
    );
    private static final UserDto USER = new UserDto(
            1L,
            "NAME",
            "PASSWORD",
            "email@gmail.com",
            Set.of(ROLE_LIST.get(1))
    );

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void userList_Should_Return_True() throws Exception {
        List<UserDto> userDtoList = List.of(USER);
        Mockito.when(userService.findByUsernameLike("%")).thenReturn(userDtoList);
        this.mockMvc.perform(
                get("/admin/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("find_user"))
                .andExpect(model().attribute("users", userDtoList));
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void userForm_Should_Return_True() throws Exception {
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        Mockito.when(roleService.findAll()).thenReturn(ROLE_LIST);
        this.mockMvc.perform(
                get("/admin/user/{id}", USER.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user_form"))
                .andExpect(model().attribute("user", USER))
                .andExpect(model().attribute("roles", ROLE_LIST));
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void newUser_Should_Return_True() throws Exception {
        Mockito.when(roleService.findAll()).thenReturn(ROLE_LIST);
        mockMvc.perform(get("/admin/user/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_form"))
                .andExpect(model().attribute("roles", ROLE_LIST));
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void submitUserForm_Should_Return_True() throws Exception {
        mockMvc.perform(post("/admin/user").with(csrf())
                .flashAttr("user", USER))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/admin/user"));
        Mockito.verify(userService, Mockito.times(1)).save(USER);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    void submitUserForm_Should_Return_ValidErrors() throws Exception {
        UserDto errorValidUserDto = new UserDto(1L, "", "PASSWORD", "email@gmail.com", Set.of(new Role("ADMIN")));
        mockMvc.perform(post("/admin/user").with(csrf())
                .flashAttr("user", errorValidUserDto))
                .andExpect(model().hasErrors())
                .andExpect(view().name("user_form"));
        Mockito.verify(userService, Mockito.times(0)).save(errorValidUserDto);
    }

    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    @Test
    @Disabled
    //TODO исправить тест
    void deleteUser_Should_Return_True() throws Exception {
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        mockMvc.perform(delete("/admin/user/{id}", USER.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user"));
        Mockito.verify(userService, Mockito.times(1)).delete(USER);
    }
}
