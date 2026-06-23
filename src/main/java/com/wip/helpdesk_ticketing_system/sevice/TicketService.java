package com.wip.helpdesk_ticketing_system.sevice;

import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.enums.Status;
import java.util.List;

public interface TicketService {
    Ticket createTicket(TicketDto dto);
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    Ticket updateTicket(Long id, TicketDto dto);
    Ticket updateStatus(Long id, Status status);
    void deleteTicket(Long id);
    List<Ticket> getTicketsByUser(Long userId);
    List<Ticket> getTicketsByStatus(Status status);
}
