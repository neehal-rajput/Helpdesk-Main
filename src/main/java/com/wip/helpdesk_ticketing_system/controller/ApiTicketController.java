package com.wip.helpdesk_ticketing_system.controller;

import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.sevice.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Ticket management APIs")
public class ApiTicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    @Operation(summary = "Create a new ticket")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT','END_USER')")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketDto dto) {
        return new ResponseEntity<>(ticketService.createTicket(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all tickets")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT','END_USER')")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ticket")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketDto dto) {
        return ResponseEntity.ok(ticketService.updateTicket(id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update ticket status")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<Ticket> updateStatus(@PathVariable Long id, @RequestParam Status status) {
        return ResponseEntity.ok(ticketService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok("Ticket deleted successfully");
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get tickets by user")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT','END_USER')")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tickets by status")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }
}
