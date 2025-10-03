package com.driven.dm.ai.application.service;

import com.driven.dm.ai.domain.entity.AiCallLog;
import com.driven.dm.ai.domain.repository.AiCallLogRepository;
import com.driven.dm.global.config.ai.OpenAiConstants;
import com.driven.dm.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final AiCallLogRepository aiCallLogRepository;

    /**
     * OpenAI 호출로 생성 & AiCallLog 에 요청/응답 저장
     * @param user 현재 사장님 유저
     * @param menuName 메뉴명
     * @param category 카테고리 (한식/중식/분식/치킨/피자)
     * @param features 주요 재료 (쉼표로 나열 가능)
     * @return AI가 생성한 메뉴 설명 텍스트
     */
    @Transactional
    public String generateMenuDescription(User user, String menuName, String category,
        String features) {

        // 1. 프롬프트 생성
        String prompt = String.format(OpenAiConstants.MENU_DESCRIPTION_PROMPT, menuName, category,
            features);

        // 2. SpringAI + OpenAI 호출
        String outputText;

        try {
            outputText = chatClient
                .prompt()       // 메시지 빌더 시작
                .user(prompt)   // user 역할 메시지로 방금 만든 프롬프트 추가
                .call()         // 실제 LLM 동기 호출 (OpenAI API 호출)
                .content();     // 응답에서 텍스트만 추출
        } catch (Exception e) {
            log.error("OpenAI 호출 실패: {}", e.getMessage(), e);
            // 장애 시 간단 fallback
            outputText = menuName + "은(는) 신선한 재료로 만든 담백한 맛이 특징입니다.";
        }

        // 3. 호출 로그 저장
        AiCallLog aiCallLog = AiCallLog.of(
            user,
            OpenAiConstants.PROVIDER_OPENAI,
            OpenAiConstants.MODEL_GPT_4O_MINI,
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