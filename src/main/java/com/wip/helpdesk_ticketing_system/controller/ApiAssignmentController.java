package com.wip.helpdesk_ticketing_system.controller;

import com.wip.helpdesk_ticketing_system.dto.AssignmentDto;
import com.wip.helpdesk_ticketing_system.entity.Assignment;
import com.wip.helpdesk_ticketing_system.sevice.AssignmentService;
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
@RequestMapping("/api/assignments")
@Tag(name = "Assignments", description = "Ticket assignment APIs")
public class ApiAssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    @Operation(summary = "Assign a ticket to an agent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Assignment> assignTicket(@Valid @RequestBody AssignmentDto dto) {
        return new ResponseEntity<>(assignmentService.assignTicket(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }
}
