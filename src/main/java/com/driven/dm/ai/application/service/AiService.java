package com.driven.dm.ai.application.service;

import com.driven.dm.ai.application.exception.AiErrorCode;
import com.driven.dm.ai.domain.entity.AiCallLog;
import com.driven.dm.ai.infrastructure.repository.AiCallLogRepository;
import com.driven.dm.ai.presentation.dto.response.AiCallLogPageResponseDto;
import com.driven.dm.ai.presentation.dto.response.AiCallLogResponseDto;
import com.driven.dm.ai.presentation.dto.response.AiCallResponseDto;
import com.driven.dm.global.config.ai.OpenAiConstants;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.service.UserReader;
import com.driven.dm.user.domain.entity.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final AiCallLogRepository aiCallLogRepository;
    private final UserReader userReader;

    private static final long DEFAULT_PAGE = 1L;
    private static final long DEFAULT_SIZE = 10L;
    private static final List<Long> PAGE_SIZE_WHITELIST = List.of(10L, 30L, 50L);

    /**
     * [OpenAI 호출로 생성 & AiCallLog 에 요청/응답 저장]
     *
     * @param userId   현재 사장님 유저
     * @param menuName 메뉴명
     * @param category 카테고리 (한식/중식/분식/치킨/피자)
     * @param features 주요 재료 (쉼표로 나열 가능)
     * @return AI가 생성한 메뉴 설명 텍스트
     */
    @Transactional
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER', 'OWNER')")
    @CircuitBreaker(name = "openai", fallbackMethod = "generateMenuDescriptionFallback")
    @Retry(name = "openai")
    @RateLimiter(name = "openai")
    public AiCallResponseDto generateMenuDescription(
        UUID userId, String menuName, String category, String features) {

        User owner = userReader.findActiveUser(userId);

        String prompt = String.format(
            OpenAiConstants.MENU_DESCRIPTION_PROMPT, menuName, category, features);

        String outputText = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content();

        AiCallLog aiCallLog = AiCallLog.of(
            owner,
            OpenAiConstants.PROVIDER_OPENAI,
            OpenAiConstants.MODEL_GPT_4O_MINI,
            prompt,
            outputText
        );
        aiCallLogRepository.save(aiCallLog);

        return AiCallResponseDto.from(aiCallLog);
    }

    /**
     * [AI 호출 로그 목록 조회]
     *
     * @param page     현재 페이지
     * @param pageSize 페이지 당 내역 수
     * @return 조회된 로그 리스트 정보와 전체 개수를 담은 DTO 객체
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public AiCallLogPageResponseDto getAiCallLogList(Long page, Long pageSize) {

        long p = normalizePage(page);
        long s = normalizePageSize(pageSize);

        List<AiCallLogResponseDto> logList = aiCallLogRepository
            .findLogsWithPaging((p - 1) * s, s)
            .stream()
            .map(AiCallLogResponseDto::from)
            .toList();

        long totalCount = aiCallLogRepository.countAllActiveLogs();

        return AiCallLogPageResponseDto.of(logList, totalCount);
    }

    /**
     * [AI 호출 로그 검색 조회]
     *
     * @param content  검색 키워드 (null 또는 공백이면 전체 목록 반환)
     * @param page     현재 페이지
     * @param pageSize 페이지 당 내역 수
     * @return 조회된 로그 리스트와 총 개수를 담은 DTO 객체
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "searchLog", key = "T(java.util.Objects).hash(#content,#page,#pageSize)")
    public AiCallLogPageResponseDto searchLogByContent(String content, Long page, Long pageSize) {

        long p = normalizePage(page);
        long s = normalizePageSize(pageSize);

        // keyword 가 null 이거나 공백이면 전체 목록을 반환
        if (content == null || content.isBlank()) {
            return getAiCallLogList(p, s);
        }

        long offset = (p - 1) * s;

        List<AiCallLogResponseDto> logList = aiCallLogRepository
            .searchByOutputTextWithPaging(content, offset, s)
            .stream()
            .map(AiCallLogResponseDto::from)
            .toList();

        long totalCount = aiCallLogRepository.countAllActiveLogsByOutputText(content);

        if (totalCount == 0) {
            throw AppException.of(AiErrorCode.AI_LOG_SEARCH_NOT_FOUND);
        }

        return AiCallLogPageResponseDto.of(logList, totalCount);
    }

    /**
     * [AI 호출 로그 단건 조회]
     *
     * @param id 조회할 로그의 UUID
     * @return 조회된 로그를 응답 DTO 로 변환한 객체
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public AiCallLogResponseDto getAiCallLog(UUID id) {

        AiCallLog aiCallLog = getLogOrThrow(id);

        if (aiCallLog.isDeleted()) {
            throw AppException.of(AiErrorCode.AI_LOG_ALREADY_DELETED);
        }

        return AiCallLogResponseDto.from(aiCallLog);
    }

    /**
     * [AI 호출 로그 단건 삭제]
     *
     * @param id            삭제할 로그의 UUID
     * @param deleterUserId 삭제를 수행하는 유저의 UUID
     */
    @Transactional
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public void deleteAiCallLog(UUID id, UUID deleterUserId) {

        AiCallLog aiCallLog = getLogOrThrow(id);

        if (aiCallLog.isDeleted()) {
            throw AppException.of(AiErrorCode.AI_LOG_ALREADY_DELETED);
        }

        aiCallLog.delete(deleterUserId);
    }

    /**
     * [AI 호출 로그 단건 복구]
     *
     * @param id             복구할 로그의 UUID
     * @param restorerUserId 복구를 수행하는 유저의 UUID
     */
    @Transactional
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public void restoreAiCallLog(UUID id, UUID restorerUserId) {

        AiCallLog aiCallLog = getLogOrThrow(id);

        if (!aiCallLog.isDeleted()) {
            throw AppException.of(AiErrorCode.AI_LOG_HAS_NOT_BEEN_DELETED);
        }

        aiCallLog.restore(restorerUserId);
    }

    // [공통] 로그 단건 조회 메서드
    private AiCallLog getLogOrThrow(UUID id) {

        return aiCallLogRepository.findById(id)
            .orElseThrow(() -> AppException.of(AiErrorCode.AI_LOG_NOT_FOUND));
    }

    // [공통] 페이징 보정 - 페이지 번호: null 또는 1 미만이면 1
    private static long normalizePage(Long page) {

        return (page == null || page < 1) ? DEFAULT_PAGE : page;
    }

    // [공통] 페이징 보정 - 페이지 크기: 화이트리스트 외/ null 이면 10
    private static long normalizePageSize(Long pageSize) {

        return (pageSize == null || !PAGE_SIZE_WHITELIST.contains(pageSize)) ? DEFAULT_SIZE
            : pageSize;
    }

    // 메뉴 설명 생성 실패 시 fallback 메서드
    private AiCallResponseDto generateMenuDescriptionFallback(
        UUID userId, String menuName, String category, String features, Throwable t) {

        log.error("OpenAI 호출 실패(서킷/재시도/레이트리밋): {}", t.toString());
        User owner = userReader.findActiveUser(userId);

        String prompt = String.format(
            OpenAiConstants.MENU_DESCRIPTION_PROMPT, menuName, category, features);
        String fallbackText = menuName + "은(는) 신선한 재료로 만든 담백한 맛이 특징입니다.";

        AiCallLog logEntity = AiCallLog.of(
            owner,
            OpenAiConstants.PROVIDER_OPENAI,
            OpenAiConstants.MODEL_GPT_4O_MINI,
            prompt,
            fallbackText
        );

        aiCallLogRepository.save(logEntity);

        return AiCallResponseDto.from(logEntity);
    }
}
