package com.wip.helpdesk_ticketing_system.repository;

import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(Status status);
    List<Ticket> findByUserUserId(Long userId);
    List<Ticket> findByUserEmail(String email);
    long countByStatus(Status status);
}
