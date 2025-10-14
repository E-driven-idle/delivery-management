package com.driven.dm.ai.presentation.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.driven.dm.ai.application.service.AiService;
import com.driven.dm.ai.presentation.dto.response.AiCallLogPageResponseDto;
import com.driven.dm.ai.presentation.dto.response.AiCallLogResponseDto;
import com.driven.dm.global.config.security.SecurityUser;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = AiController.class)
@Import(AiControllerTest.TestConfig.class)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AiService aiService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        AiService aiService() {
            return Mockito.mock(AiService.class);
        }
    }

    // SecurityUser 모킹해 Authentication 만들기
    private UsernamePasswordAuthenticationToken authWithSecurityUser(UUID userId, String role) {

        SecurityUser principal = Mockito.mock(SecurityUser.class);
        when(principal.getId()).thenReturn(userId);

        return new UsernamePasswordAuthenticationToken(
            principal,
            "N/A",
            List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }

    @Nested
    class GetLog {

        @Test
        @DisplayName("GET /api/v1/ai/logs — 로그 목록 조회 MANAGER 권한이면 200 OK")
        void getLogList_manager_ok() throws Exception {

            // [given]
            var auth = authWithSecurityUser(UUID.randomUUID(), "MANAGER");
            AiCallLogPageResponseDto pageStub = AiCallLogPageResponseDto.of(List.of(), 0L);
            when(aiService.getAiCallLogList(1L, 10L)).thenReturn(pageStub);

            // [when]
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/v1/ai/logs")
                        .with(authentication(auth))
                        .param("page", "1")
                        .param("pageSize", "10")
                )

                // [then]
                .andExpect(status().isOk());

            verify(aiService).getAiCallLogList(1L, 10L);
        }

        @Test
        @DisplayName("GET /api/v1/ai/logs/{id} — 로그 단건 조회 MANAGER 권한이면 200 OK")
        void getLog_manager_ok() throws Exception {

            // [given]
            var auth = authWithSecurityUser(UUID.randomUUID(), "MANAGER");
            UUID id = UUID.randomUUID();
            AiCallLogResponseDto stub = Mockito.mock(AiCallLogResponseDto.class);
            when(aiService.getAiCallLog(id)).thenReturn(stub);

            // [when]
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/v1/ai/logs/{id}", id)
                        .with(authentication(auth))
                )

                // [then]
                .andExpect(status().isOk());

            verify(aiService).getAiCallLog(id);
        }

        @Test
        @DisplayName("GET /api/v1/ai/logs/search — 로그 검색 조회 MANAGER 권한이면 200 OK")
        void searchLog_manager_ok() throws Exception {

            // [given]
            var auth = authWithSecurityUser(UUID.randomUUID(), "MANAGER");
            AiCallLogPageResponseDto pageStub = AiCallLogPageResponseDto.of(List.of(), 0L);
            when(aiService.searchLogByContent("탕수육", 2L, 30L)).thenReturn(pageStub);

            // [when]
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/api/v1/ai/logs/search")
                        .with(authentication(auth))
                        .param("keyword", "탕수육")
                        .param("page", "2")
                        .param("pageSize", "30")
                )

                // [then]
                .andExpect(status().isOk());

            verify(aiService).searchLogByContent("탕수육", 2L, 30L);
        }
    }
}
