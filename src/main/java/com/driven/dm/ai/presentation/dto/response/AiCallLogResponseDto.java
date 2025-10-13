package com.driven.dm.ai.presentation.dto.response;

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
public class AiCallLogResponseDto {

    private UUID id;
    private String aiProvider;
    private String model;
    private String prompt;
    private String outputText;
    private LocalDateTime createdAt;
    private UUID createdBy;

    public static AiCallLogResponseDto from(AiCallLog aiCallLog) {

        return AiCallLogResponseDto.builder()
            .id(aiCallLog.getId())
            .aiProvider(aiCallLog.getAiProvider())
            .model(aiCallLog.getModel())
            .prompt(aiCallLog.getPrompt())
            .outputText(aiCallLog.getOutputText())
            .createdAt(aiCallLog.getCreatedAt())
            .createdBy(aiCallLog.getUser().getId())
            .build();
    }
}
