package com.driven.dm.ai.application.service;

import com.driven.dm.ai.domain.entity.AiCallLog;
import com.driven.dm.ai.domain.repository.AiCallLogRepository;
import com.driven.dm.global.config.ai.OpenAiConstants;
import com.driven.dm.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final AiCallLogRepository aiCallLogRepository;

    // OpenAI 호출로 생성 & AiCallLog 에 요청/응답 저장
    @Transactional
    public String generateMenuDescription(User user, String menuName) {

        // 1. 프롬프트 생성
        String prompt = String.format(OpenAiConstants.MENU_DESCRIPTION_PROMPT, menuName);

        // 2. SpringAI + OpenAI 호출
        String outputText;

        try {
            outputText = chatClient
                .prompt()       // 메시지 빌더 시작
                .user(prompt)   // user 역할 메시지로 방금 만든 프롬프트 추가
                .call()         // 실제 LLM 동기 호출 (OpenAI API 호출)
                .content();     // 응답에서 텍스트만 추출
        } catch (Exception e) {
            // 장애 시 간단 fallback
            outputText = menuName + "은(는) 신선한 재료로 만든 담백한 맛이 특징입니다.";
        }

        // 3. 호출 로그 저장
        AiCallLog aiCallLog = AiCallLog.of(
            user,
            OpenAiConstants.PROVIDER_OPENAI,
            OpenAiConstants.MODEL_GRP_4O_MINI,
            prompt,
            outputText
        );
        aiCallLogRepository.save(aiCallLog);

        return outputText;
    }
}

/* TODO
 * 장애 시 간단 fallback 상세 처리 필요
 */