package com.driven.dm.ai.presentation.dto.response;

import java.util.List;

public record AiCallLogPageResponseDto(List<AiCallLogResponseDto> logList, Long count) {

    public static AiCallLogPageResponseDto of(List<AiCallLogResponseDto> logList, Long count) {

        return new AiCallLogPageResponseDto(logList, count);
    }
}
