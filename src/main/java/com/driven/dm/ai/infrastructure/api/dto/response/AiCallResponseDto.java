package com.driven.dm.ai.infrastructure.api.dto.response;

import com.driven.dm.ai.domain.entity.AiCallLog;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AiCallResponseDto {

    private UUID id;
    private String outputText;
    private LocalDateTime createdAt;

    public static AiCallResponseDto from(AiCallLog aiCallLog) {

        return AiCallResponseDto.builder()
            .id(aiCallLog.getId())
            .outputText(aiCallLog.getOutputText())
            .createdAt(aiCallLog.getCreatedAt())
            .build();
    }
}
