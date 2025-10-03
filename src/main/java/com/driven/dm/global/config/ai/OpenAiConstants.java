package com.driven.dm.global.config.ai;

public class OpenAiConstants {

    private OpenAiConstants() {
    }

    public static final String PROVIDER_OPENAI = "OpenAI";

    // 필요 시 모델 상수를 여기서 관리 (yml 기본값을 덮어쓸 때 사용)
    public static final String MODEL_GPT_4O_MINI = "gpt-4o-mini";

    public static final String MENU_DESCRIPTION_PROMPT =
        """
            당신은 음식점 사장님을 돕는 카피라이터입니다.
            아래 정보를 바탕으로 메뉴 설명을 한국어로 200~250자 내외, 사실 기반으로 작성하세요.
            과장 표현은 줄이고, 고객이 이해하기 쉽게 표현하세요.
            
            - 메뉴명: %s
            - 카테고리: %s
            - 주요 재료/특징: %s
            
            출력은 순수 텍스트만 반환하세요.
            """;
}

/* TODO
 * Few-Shot Prompting 필요
 */