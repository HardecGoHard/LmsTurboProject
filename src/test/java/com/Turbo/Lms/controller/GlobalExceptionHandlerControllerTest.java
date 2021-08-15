package com.Turbo.Lms.controller;

import com.Turbo.Lms.service.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandlerController.class)
public class GlobalExceptionHandlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final Long INCORRECT_ID = 1234567890L;

    @Test
    @WithMockUser(username = "user", roles = RoleType.ADMIN_WITHOUT_PREFIX)
    void notFoundException_Should_True() throws Exception {
        mockMvc.perform(get("/course/" + INCORRECT_ID))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get("/lesson/" + INCORRECT_ID))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get("/admin/user/" + INCORRECT_ID))
                .andExpect(status().is4xxClientError());

    }

}
