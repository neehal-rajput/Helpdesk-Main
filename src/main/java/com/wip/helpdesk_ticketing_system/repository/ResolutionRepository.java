package com.wip.helpdesk_ticketing_system.repository;

import com.wip.helpdesk_ticketing_system.entity.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {
}
