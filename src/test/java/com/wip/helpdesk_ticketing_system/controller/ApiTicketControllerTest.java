package com.wip.helpdesk_ticketing_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Priority;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.sevice.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiTicketController.class)
class ApiTicketControllerTest {

    // MockMvc simulates HTTP requests without starting real server
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper converts Java objects to JSON
    @Autowired
    private ObjectMapper objectMapper;

    // MockBean creates fake service - no real database needed
    @MockBean
    private TicketService ticketService;

    private Ticket ticket;
    private Ticket ticket2;
    private TicketDto ticketDto;
    private User user;

    // Runs before every test - sets up sample data
    @BeforeEach
    void setup() {
        user = new User();
        user.setUserId(1L);
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setRole(Role.END_USER);

        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setTitle("Network is down");
        ticket.setDescription("Cannot connect to internet");
        ticket.setPriority(Priority.HIGH);
        ticket.setStatus(Status.OPEN);
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());
        ticket.setUser(user);

        ticket2 = new Ticket();
        ticket2.setTicketId(2L);
        ticket2.setTitle("Printer not working");
        ticket2.setDescription("Printer gives error");
        ticket2.setPriority(Priority.MEDIUM);
        ticket2.setStatus(Status.IN_PROGRESS);
        ticket2.setCreatedDate(LocalDateTime.now());
        ticket2.setUpdatedDate(LocalDateTime.now());
        ticket2.setUser(user);

        ticketDto = new TicketDto();
        ticketDto.setTitle("Network is down");
        ticketDto.setDescription("Cannot connect to internet");
        ticketDto.setPriority(Priority.HIGH);
        ticketDto.setUserId(1L);
    }

    // =============================================
    // TEST 1 - GET ALL TICKETS - ADMIN CAN ACCESS
    // =============================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTickets_adminCanAccess_returns200() throws Exception {

        // Tell fake service what to return
        List<Ticket> tickets = Arrays.asList(ticket, ticket2);
        when(ticketService.getAllTickets()).thenReturn(tickets);

        // Perform GET request and check response
        mockMvc.perform(get("/api/tickets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].ticketId").value(1))
                .andExpect(jsonPath("$[0].title").value("Network is down"))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].ticketId").value(2))
                .andExpect(jsonPath("$[1].title").value("Printer not working"));
    }

    // =============================================
    // TEST 2 - GET ALL TICKETS - AGENT CAN ACCESS
    // =============================================
    @Test
    @WithMockUser(roles = "AGENT")
    void getAllTickets_agentCanAccess_returns200() throws Exception {

        List<Ticket> tickets = Arrays.asList(ticket, ticket2);
        when(ticketService.getAllTickets()).thenReturn(tickets);

        mockMvc.perform(get("/api/tickets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

//    // =============================================
//    // TEST 3 - GET ALL TICKETS - END USER BLOCKED
//    // =============================================
//    @Test
//    @WithMockUser(roles = "END_USER")
//    void getAllTickets_endUserBlocked_returns403() throws Exception {
//
//        mockMvc.perform(get("/api/tickets"))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//
//    // =============================================
//    // TEST 4 - GET ALL TICKETS - NOT LOGGED IN
//    // =============================================
//    @Test
//    void getAllTickets_notLoggedIn_returns401() throws Exception {
//
//        mockMvc.perform(get("/api/tickets"))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//
//    // =============================================
//    // TEST 5 - GET TICKET BY ID - SUCCESS
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void getTicketById_exists_returns200() throws Exception {
//
//        when(ticketService.getTicketById(1L)).thenReturn(ticket);
//
//        mockMvc.perform(get("/api/tickets/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.ticketId").value(1))
//                .andExpect(jsonPath("$.title").value("Network is down"))
//                .andExpect(jsonPath("$.priority").value("HIGH"))
//                .andExpect(jsonPath("$.status").value("OPEN"));
//    }
//
//    // =============================================
//    // TEST 6 - CREATE TICKET - ADMIN SUCCESS
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void createTicket_adminRole_returns201() throws Exception {
//
//        when(ticketService.createTicket(any(TicketDto.class))).thenReturn(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.ticketId").value(1))
//                .andExpect(jsonPath("$.title").value("Network is down"))
//                .andExpect(jsonPath("$.status").value("OPEN"));
//    }
//
//    // =============================================
//    // TEST 7 - CREATE TICKET - END USER SUCCESS
//    // =============================================
//    @Test
//    @WithMockUser(roles = "END_USER")
//    void createTicket_endUserRole_returns201() throws Exception {
//
//        when(ticketService.createTicket(any(TicketDto.class))).thenReturn(ticket);
//
//        mockMvc.perform(post("/api/tickets")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(ticketDto)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//    }
//
//    // =============================================
//    // TEST 8 - UPDATE TICKET STATUS - ADMIN
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void updateStatus_adminRole_returns200() throws Exception {
//
//        ticket.setStatus(Status.IN_PROGRESS);
//        when(ticketService.updateStatus(eq(1L), eq(Status.IN_PROGRESS)))
//                .thenReturn(ticket);
//
//        mockMvc.perform(patch("/api/tickets/1/status")
//                        .with(csrf())
//                        .param("status", "IN_PROGRESS"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
//    }
//
//    // =============================================
//    // TEST 9 - UPDATE TICKET STATUS - AGENT
//    // =============================================
//    @Test
//    @WithMockUser(roles = "AGENT")
//    void updateStatus_agentRole_returns200() throws Exception {
//
//        ticket.setStatus(Status.RESOLVED);
//        when(ticketService.updateStatus(eq(1L), eq(Status.RESOLVED)))
//                .thenReturn(ticket);
//
//        mockMvc.perform(patch("/api/tickets/1/status")
//                        .with(csrf())
//                        .param("status", "RESOLVED"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    // =============================================
//    // TEST 10 - DELETE TICKET - ADMIN SUCCESS
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void deleteTicket_adminRole_returns200() throws Exception {
//
//        doNothing().when(ticketService).deleteTicket(1L);
//
//        mockMvc.perform(delete("/api/tickets/1")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    // =============================================
//    // TEST 11 - DELETE TICKET - AGENT BLOCKED
//    // =============================================
//    @Test
//    @WithMockUser(roles = "AGENT")
//    void deleteTicket_agentRole_returns403() throws Exception {
//
//        mockMvc.perform(delete("/api/tickets/1")
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//
//    // =============================================
//    // TEST 12 - GET TICKETS BY USER ID
//    // =============================================
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void getTicketsByUser_returns200() throws Exception {
//
//        when(ticketService.getTicketsByUser(1L))
//                .thenReturn(Arrays.asList(ticket));
//
//        mockMvc.perform(get("/api/tickets/user/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].ticketId").value(1));
//    }
}