package com.wip.helpdesk_ticketing_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.helpdesk_ticketing_system.dto.UserDto;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.sevice.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiUserController.class)
class ApiUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User adminUser;
    private User agentUser;
    private User endUser;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        adminUser = new User();
        adminUser.setUserId(1L);
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@test.com");
        adminUser.setPasswordHash("encoded");
        adminUser.setRole(Role.ADMIN);

        agentUser = new User();
        agentUser.setUserId(2L);
        agentUser.setName("Agent User");
        agentUser.setEmail("agent@test.com");
        agentUser.setPasswordHash("encoded");
        agentUser.setRole(Role.AGENT);

        endUser = new User();
        endUser.setUserId(3L);
        endUser.setName("End User");
        endUser.setEmail("enduser@test.com");
        endUser.setPasswordHash("encoded");
        endUser.setRole(Role.END_USER);

        userDto = new UserDto();
        userDto.setName("New User");
        userDto.setEmail("newuser@test.com");
        userDto.setPassword("pass123");
        userDto.setRole(Role.END_USER);
    }

    // =============================================
    // TEST 1 - GET ALL USERS - ADMIN SUCCESS
    // =============================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_adminRole_returns200() throws Exception {

        List<User> users = Arrays.asList(adminUser, agentUser, endUser);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Admin User"))
                .andExpect(jsonPath("$[1].name").value("Agent User"))
                .andExpect(jsonPath("$[2].name").value("End User"));
    }

//    // =============================================
//    // TEST 2 - GET ALL USERS - AGENT BLOCKED
//    // =============================================
//    @Test
//    @WithMockUser(roles = "AGENT")
//    void getAllUsers_agentRole_returns403() throws Exception {
//
//        mockMvc.perform(get("/api/users"))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }

//    // =============================================
//    // TEST 3 - GET ALL USERS - END USER BLOCKED
//    // =============================================
//    @Test
//    @WithMockUser(roles = "END_USER")
//    void getAllUsers_endUserRole_returns403() throws Exception {
//
//        mockMvc.perform(get("/api/users"))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//
    // =============================================
    // TEST 4 - GET ALL USERS - NOT LOGGED IN
    // =============================================
    @Test
    void getAllUsers_notLoggedIn_returns401() throws Exception {

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

//    // =============================================
//    // TEST 5 - GET USER BY ID - ADMIN SUCCESS
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void getUserById_adminRole_returns200() throws Exception {
//
//        when(userService.getUserById(1L)).thenReturn(adminUser);
//
//        mockMvc.perform(get("/api/users/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(1))
//                .andExpect(jsonPath("$.name").value("Admin User"))
//                .andExpect(jsonPath("$.email").value("admin@test.com"))
//                .andExpect(jsonPath("$.role").value("ADMIN"));
//    }
//
//    // =============================================
//    // TEST 6 - CREATE USER - ADMIN SUCCESS
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void createUser_adminRole_returns201() throws Exception {
//
//        User newUser = new User();
//        newUser.setUserId(4L);
//        newUser.setName("New User");
//        newUser.setEmail("newuser@test.com");
//        newUser.setRole(Role.END_USER);
//
//        when(userService.addUser(any(UserDto.class))).thenReturn(newUser);
//
//        mockMvc.perform(post("/api/users")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userId").value(4))
//                .andExpect(jsonPath("$.name").value("New User"))
//                .andExpect(jsonPath("$.role").value("END_USER"));
//    }
//
//    // =============================================
//    // TEST 7 - CREATE USER - AGENT BLOCKED
//    // =============================================
//    @Test
//    @WithMockUser(roles = "AGENT")
//    void createUser_agentRole_returns403() throws Exception {
//
//        mockMvc.perform(post("/api/users")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//
//    // =============================================
//    // TEST 8 - DELETE USER - ADMIN SUCCESS
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void deleteUser_adminRole_returns200() throws Exception {
//
//        doNothing().when(userService).deleteUser(1L);
//
//        mockMvc.perform(delete("/api/users/1")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    // =============================================
//    // TEST 9 - DELETE USER - END USER BLOCKED
//    // =============================================
//    @Test
//    @WithMockUser(roles = "END_USER")
//    void deleteUser_endUserRole_returns403() throws Exception {
//
//        mockMvc.perform(delete("/api/users/1")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
}