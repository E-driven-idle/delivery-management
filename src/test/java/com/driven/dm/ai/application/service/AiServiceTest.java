package com.driven.dm.ai.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.driven.dm.ai.domain.entity.AiCallLog;
import com.driven.dm.ai.infrastructure.repository.AiCallLogRepository;
import com.driven.dm.ai.presentation.dto.response.AiCallLogPageResponseDto;
import com.driven.dm.ai.presentation.dto.response.AiCallResponseDto;
import com.driven.dm.user.application.service.UserReader;
import com.driven.dm.user.domain.entity.User;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.ai.chat.client.ChatClient;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    @Mock
    private AiCallLogRepository aiCallLogRepository;

    @Mock
    private UserReader userReader;

    @InjectMocks
    private AiService aiService;

    @Nested
    class generateMenuDescription {

        @BeforeEach
        void setUp() {

            when(aiCallLogRepository.save(any(AiCallLog.class)))
                .thenAnswer((Answer<AiCallLog>) inv -> inv.getArgument(0, AiCallLog.class));
        }

        @Test
        @DisplayName("메뉴 설명 생성 성공: LLM 응답을 그대로 사용하고 로그가 저장된다")
        void generateMenuDescription_success() {

            // [given]
            UUID userId = UUID.randomUUID();
            User owner = mock(User.class);
            when(userReader.findActiveUser(userId))
                .thenReturn(owner);

            String menuName = "불고기덮밥";
            String category = "한식";
            String features = "불고기, 양파, 간장소스";
            String outputText = "달큼한 간장 소스에 볶아낸 불고기를 따끈한 밥 위에 올린 메뉴입니다.";

            when(chatClient.prompt().user(anyString()).call().content())
                .thenReturn(outputText);

            // [when]
            AiCallResponseDto dto = aiService.generateMenuDescription(userId, menuName, category,
                features);

            // [then]
            assertThat(dto).isNotNull();
            assertThat(dto.getOutputText()).isEqualTo(outputText);

            ArgumentCaptor<AiCallLog> captor = ArgumentCaptor.forClass(AiCallLog.class);
            verify(aiCallLogRepository, times(1)).save(captor.capture());
            AiCallLog saved = captor.getValue();

            assertThat(saved.getUser()).isEqualTo(owner);
            assertThat(saved.getPrompt()).contains(menuName).contains(category).contains(features);
            assertThat(saved.getOutputText()).isEqualTo(outputText);
        }

        @Test
        @DisplayName("메뉴 설명 생성 예외 발생 시: fallback 문구로 저장된다")
        void generateMenuDescription_fallback_onException() {

            // [given]
            UUID userId = UUID.randomUUID();
            User owner = mock(User.class);
            when(userReader.findActiveUser(userId))
                .thenReturn(owner);

            String menuName = "크림파스타";
            String category = "양식";
            String features = "생크림, 파르미지아노";

            when(chatClient.prompt().user(anyString()).call().content())
                .thenThrow(new RuntimeException("OpenAI down"));

            // [when]
            AiCallResponseDto dto = aiService.generateMenuDescription(userId, menuName, category,
                features);

            // [then]
            String expectedFallback = menuName + "은(는) 신선한 재료로 만든 담백한 맛이 특징입니다.";
            assertThat(dto).isNotNull();
            assertThat(dto.getOutputText()).isEqualTo(expectedFallback);

            ArgumentCaptor<AiCallLog> captor = ArgumentCaptor.forClass(AiCallLog.class);
            verify(aiCallLogRepository, times(1)).save(captor.capture());
            AiCallLog saved = captor.getValue();

            assertThat(saved.getUser()).isEqualTo(owner);
            assertThat(saved.getPrompt()).contains(menuName).contains(category).contains(features);
            assertThat(saved.getOutputText()).isEqualTo(expectedFallback);
        }
    }

    @Nested
    class getAiCallLog {

        @Test
        @DisplayName("로그 목록 조회: 정상 페이징(page=2, size=10) → offset=10/limit=10으로 조회된다")
        void getAiCallLogList_success_paging() {

            // [given]
            long page = 2L;     // (p - 1) * s = 10
            long pageSize = 10L;

            User owner = mock(User.class);
            UUID ownerId = UUID.randomUUID();
            when(owner.getId()).thenReturn(ownerId);

            AiCallLog log1 = AiCallLog.of(owner, "OpenAi", "gpt-4o-mini", "prompt-1", "output-1");
            AiCallLog log2 = AiCallLog.of(owner, "OpenAi", "gpt-4o-mini", "prompt-2", "output-2");

            when(aiCallLogRepository.findLogsWithPaging(eq(10L), eq(10L)))
                .thenReturn(List.of(log1, log2));
            when(aiCallLogRepository.countAllActiveLogs())
                .thenReturn(25L);

            // [when]
            AiCallLogPageResponseDto result = aiService.getAiCallLogList(page, pageSize);

            // [then]
            assertThat(result).isNotNull();
            assertThat(result.count()).isEqualTo(25L);
            assertThat(result.logList()).hasSize(2);
            assertThat(result.logList().get(0).getOutputText()).isEqualTo("output-1");
            assertThat(result.logList().get(1).getOutputText()).isEqualTo("output-2");
            assertThat(result.logList().get(0).getCreatedBy()).isEqualTo(ownerId);

            verify(aiCallLogRepository, times(1)).findLogsWithPaging(10L, 10L);
            verify(aiCallLogRepository, times(1)).countAllActiveLogs();
            verifyNoMoreInteractions(aiCallLogRepository);
        }

        @Test
        @DisplayName("로그 목록 조회: 보정 로직(page=null, size=7 → page=1, size=10) → offset=0/limit=10")
        void getAiCallLogList_normalization_applied() {

            // [given]
            Long page = null;    // normalizePage → 1
            Long pageSize = 7L;  // 화이트리스트(10,30,50) 외 → normalizePageSize → 10

            User owner = mock(User.class);
            UUID ownerId = UUID.randomUUID();
            when(owner.getId()).thenReturn(ownerId);

            AiCallLog log = AiCallLog.of(owner, "OpenAi", "gpt-4o-mini", "prompt-x", "output-x");

            when(aiCallLogRepository.findLogsWithPaging(eq(0L), eq(10L)))
                .thenReturn(List.of(log));
            when(aiCallLogRepository.countAllActiveLogs())
                .thenReturn(1L);

            // [when]
            AiCallLogPageResponseDto result = aiService.getAiCallLogList(page, pageSize);

            // [then]
            assertThat(result.count()).isEqualTo(1L);
            assertThat(result.logList()).hasSize(1);
            assertThat(result.logList().get(0).getOutputText()).isEqualTo("output-x");
            assertThat(result.logList().get(0).getCreatedBy()).isEqualTo(ownerId);

            verify(aiCallLogRepository, times(1)).findLogsWithPaging(0L, 10L);
            verify(aiCallLogRepository, times(1)).countAllActiveLogs();
            verifyNoMoreInteractions(aiCallLogRepository);
        }
    }
}
