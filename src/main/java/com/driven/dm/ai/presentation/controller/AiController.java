package com.driven.dm.ai.presentation.controller;

import com.driven.dm.ai.application.service.AiService;
import com.driven.dm.ai.infrastructure.api.dto.response.AiCallLogResponseDto;
import com.driven.dm.ai.infrastructure.api.dto.response.AiCallResponseDto;
import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.user.application.service.UserService;
import com.driven.dm.user.domain.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;
    private final UserService userService;

    /**
     * [AI 기반 메뉴 설명 자동 생성]
     * 사용자가 등록한 메뉴 정보(메뉴명, 카테고리, 주요 재료)를 바탕으로 OpenAI 모델을 호출하여 200~250자 내외의 설명 문구를 생성
     * 생성된 설명은 AiService 를 통해 DB 로그에 기록
     *
     * @param principal 현재 로그인한 사장님 유저
     * @param menuName  메뉴명
     * @param category  카테고리 (한식/중식/분식/치킨/피자)
     * @param features  주요 재료/특징 (쉼표로 구분, 예: 돼지고기, 춘장, 양파)
     * @return AI가 생성한 메뉴 설명 텍스트
     */
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER', 'OWNER')")
    @PostMapping("/generate-description")
    public ResponseEntity<AiCallResponseDto> generateMenuDescription(
        @AuthenticationPrincipal SecurityUser principal,
        @RequestParam String menuName,
        @RequestParam String category,
        @RequestParam String features) {

        User user = userService.getActiveUser(principal.getId());

        return ResponseEntity.ok(aiService.generateMenuDescription(user, menuName, category, features));
    }

    /**
     * [AI 호출 로그 단건 조회]
     * MASTER, MANAGER 만 접근 가능
     *
     * @param id 조회할 로그의 UUID
     * @return 조회된 로그 정보를 담은 DTO 객체
     */
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @GetMapping("/logs/{id}")
    public ResponseEntity<AiCallLogResponseDto> getAiCallLog(
        @PathVariable("id") UUID id) {

        return ResponseEntity.ok(aiService.getAiCallLog(id));
    }

//    /**
//     * [AI 호출 로그 단건 삭제]
//     * MASTER, MANAGER 만 접근 가능
//     *
//     * @param principal 현재 로그인한 유저
//     * @param id 삭제할 로그의 UUID
//     * @return 삭제 성공 메시지
//     */
//    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
//    @DeleteMapping("/logs/{id}")
//    public ResponseEntity<String> deleteAiCallLog(
//        @AuthenticationPrincipal SecurityUser principal,
//        @PathVariable("id") UUID id) {
//
//        aiService.deleteAiCallLog(id, principal.getId());
//
//        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
//    }

//    /**
//     * [AI 호출 로그 단건 복구]
//     * MASTER, MANAGER 만 가능
//     * softDelete 된 로그 내역의 delete_at & deleted_by 값을 null 로 만들어 복구
//     *
//     * @param principal 현재 로그인한 유저
//     * @param id 복구할 로그의 UUID
//     * @return 복구 성공 메시지
//     */
//    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
//    @PatchMapping("/logs/{id}")
//    public ResponseEntity<String> restoreAiCallLog(
//        @AuthenticationPrincipal SecurityUser principal,
//        @PathVariable("id") UUID id) {
//
//        aiService.restoreAiCallLog(id, principal.getId());
//
//        return ResponseEntity.ok("성공적으로 복구되었습니다.");
//    }
}
