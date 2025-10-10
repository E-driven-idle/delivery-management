package com.driven.dm.ai.infrastructure.repository;

import com.driven.dm.ai.domain.entity.AiCallLog;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiCallLogRepository extends JpaRepository<AiCallLog, UUID> {

    Optional<AiCallLog> findById(UUID id);

    // 목록: soft delete 제외 + 최신순 (created_at desc, ai_id desc)으로 페이징
    @Query(
        value =
            "select p.* " +
                "from ( " +
                "  select l.ai_id, l.created_at " +
                "  from p_ai_call_log l " +
                "  where l.deleted_at is null " +
                "  order by l.created_at desc, l.ai_id desc " +
                "  limit :limit offset :offset " +
                ") t " +
                "join p_ai_call_log p on p.ai_id = t.ai_id " +
                "order by t.created_at desc, t.ai_id desc",
        nativeQuery = true
    )
    List<AiCallLog> findLogsWithPaging(
        @Param("offset") Long offset,
        @Param("limit") Long limit
    );

    // 총 개수
    @Query(
        value =
            "select count(*) " +
                "from p_ai_call_log l " +
                "where l.deleted_at is null",
        nativeQuery = true
    )
    long countAllActiveLogs();

}
