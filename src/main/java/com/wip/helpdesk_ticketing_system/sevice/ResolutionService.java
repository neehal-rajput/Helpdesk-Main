package com.wip.helpdesk_ticketing_system.sevice;

import com.wip.helpdesk_ticketing_system.dto.ResolutionDto;
import com.wip.helpdesk_ticketing_system.entity.Resolution;
import java.util.List;

public interface ResolutionService {
    Resolution resolveTicket(ResolutionDto dto);
    List<Resolution> getAllResolutions();
}
