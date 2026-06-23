package com.wip.helpdesk_ticketing_system.repository;

import com.wip.helpdesk_ticketing_system.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByTicketTicketId(Long ticketId);
}
