package com.driven.dm.ai.presentation.controller;

import com.driven.dm.ai.application.service.AiService;
import com.driven.dm.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    /**
     * [AI 기반 메뉴 설명 자동 생성 API]
     * 사용자가 등록한 메뉴 정보(메뉴명, 카테고리, 주요 재료)를 바탕으로 OpenAI 모델을 호출하여 200~250자 내외의
     * 설명 문구를 생성 생성된 설명은 AiService 를 통해 DB 로그에 기록
     *
     * @param user     현재 로그인한 사장님 유저
     * @param menuName 메뉴명
     * @param category 카테고리 (한식/중식/분식/치킨/피자)
     * @param features 주요 재료/특징 (쉼표로 구분, 예: 돼지고기, 춘장, 양파)
     * @return AI가 생성한 메뉴 설명 텍스트
     */
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER', 'OWNER')")
    @PostMapping("/generate-description")
    public ResponseEntity<String> generateMenuDescription(
        @AuthenticationPrincipal User user,
        @RequestParam String menuName,
        @RequestParam String category,
        @RequestParam String features) {

        String aiDescription = aiService.generateMenuDescription(user, menuName, category,
            features);

        return ResponseEntity.ok(aiDescription);
    }
}
