package com.wip.helpdesk_ticketing_system.sevice;

import com.wip.helpdesk_ticketing_system.dto.ResolutionDto;
import com.wip.helpdesk_ticketing_system.entity.Resolution;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.exception.TicketNotFoundException;
import com.wip.helpdesk_ticketing_system.exception.UserNotFoundException;
import com.wip.helpdesk_ticketing_system.repository.ResolutionRepository;
import com.wip.helpdesk_ticketing_system.repository.TicketRepository;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResolutionServiceImpl implements ResolutionService {

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Resolution resolveTicket(ResolutionDto dto) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new TicketNotFoundException(dto.getTicketId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        Resolution resolution = new Resolution();
        resolution.setTicket(ticket);
        resolution.setResolvedBy(user);
        resolution.setResolutionNotes(dto.getResolutionNotes());
        resolution.setResolvedDate(LocalDateTime.now());

        ticket.setStatus(Status.RESOLVED);
        ticketRepository.save(ticket);

        return resolutionRepository.save(resolution);
    }

    @Override
    public List<Resolution> getAllResolutions() {
        return resolutionRepository.findAll();
    }
}
