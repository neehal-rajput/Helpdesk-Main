package com.wip.helpdesk_ticketing_system.sevice;

import com.wip.helpdesk_ticketing_system.dto.AssignmentDto;
import com.wip.helpdesk_ticketing_system.entity.Assignment;
import java.util.List;

public interface AssignmentService {
    Assignment assignTicket(AssignmentDto dto);
    List<Assignment> getAllAssignments();
    Assignment getAssignmentById(Long id);
}
