package com.wip.helpdesk_ticketing_system.dto;

import jakarta.validation.constraints.NotNull;

public class AssignmentDto {

    @NotNull(message = "Ticket ID is required")
    private Long ticketId;

    @NotNull(message = "Agent ID is required")
    private Long agentId;

    public AssignmentDto() {}

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }
}
