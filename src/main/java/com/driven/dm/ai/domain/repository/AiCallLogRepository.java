package com.driven.dm.ai.domain.repository;

import com.driven.dm.ai.domain.entity.AiCallLog;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiCallLogRepository extends JpaRepository<AiCallLog, UUID> {

    Optional<AiCallLog> findById(UUID id);
}
