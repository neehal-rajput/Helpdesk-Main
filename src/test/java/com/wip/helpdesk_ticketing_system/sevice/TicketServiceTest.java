package com.wip.helpdesk_ticketing_system.sevice;

import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Priority;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.TicketRepository;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User user;
    private Ticket ticket;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUserId(1L);
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setRole(Role.END_USER);

        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setTitle("Fix bug");
        ticket.setDescription("Something broke");
        ticket.setPriority(Priority.HIGH);
        ticket.setStatus(Status.OPEN);
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());
        ticket.setUser(user);
    }

    @Test
    void createTicket_shouldSetStatusToOpen() {
        TicketDto dto = new TicketDto();
        dto.setTitle("Fix bug");
        dto.setDescription("Something broke");
        dto.setPriority(Priority.HIGH);
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any())).thenReturn(ticket);

        Ticket result = ticketService.createTicket(dto);

        assertThat(result.getStatus()).isEqualTo(Status.OPEN);
    }

    @Test
    void getTicketById_shouldReturnTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.getTicketById(1L);

        assertThat(result.getTitle()).isEqualTo("Fix bug");
    }

    @Test
    void getTicketById_shouldThrowWhenNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(99L))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void updateStatus_shouldChangeStatus() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Ticket result = ticketService.updateStatus(1L, Status.IN_PROGRESS);

        assertThat(result.getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    void deleteTicket_shouldCallRepository() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketRepository).delete(ticket);

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).delete(ticket);
    }
}