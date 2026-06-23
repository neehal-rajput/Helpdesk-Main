package com.wip.helpdesk_ticketing_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResolutionDto {

    @NotNull(message = "Ticket ID is required")
    private Long ticketId;

    @NotNull(message = "Resolver user ID is required")
    private Long userId;

    @NotBlank(message = "Resolution notes are required")
    private String resolutionNotes;

    public ResolutionDto() {}

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
}
