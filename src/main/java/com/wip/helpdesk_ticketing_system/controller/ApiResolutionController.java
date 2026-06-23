package com.wip.helpdesk_ticketing_system.controller;

import com.wip.helpdesk_ticketing_system.dto.ResolutionDto;
import com.wip.helpdesk_ticketing_system.entity.Resolution;
import com.wip.helpdesk_ticketing_system.sevice.ResolutionService;
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
@RequestMapping("/api/resolutions")
@Tag(name = "Resolutions", description = "Ticket resolution APIs")
public class ApiResolutionController {

    @Autowired
    private ResolutionService resolutionService;

    @PostMapping
    @Operation(summary = "Resolve a ticket")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<Resolution> resolveTicket(@Valid @RequestBody ResolutionDto dto) {
        return new ResponseEntity<>(resolutionService.resolveTicket(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all resolutions")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<List<Resolution>> getAllResolutions() {
        return ResponseEntity.ok(resolutionService.getAllResolutions());
    }
}
