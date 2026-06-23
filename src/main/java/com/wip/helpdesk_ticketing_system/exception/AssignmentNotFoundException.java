package com.wip.helpdesk_ticketing_system.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(Long id) {
        super("Assignment not found with ID: " + id);
    }
}
