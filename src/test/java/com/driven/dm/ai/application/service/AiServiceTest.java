package com.driven.dm.ai.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.driven.dm.ai.domain.entity.AiCallLog;
import com.driven.dm.ai.infrastructure.repository.AiCallLogRepository;
import com.driven.dm.ai.presentation.dto.response.AiCallResponseDto;
import com.driven.dm.user.application.service.UserReader;
import com.driven.dm.user.domain.entity.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @BeforeEach
    void setUp() {

        when(aiCallLogRepository.save(any(AiCallLog.class)))
            .thenAnswer((Answer<AiCallLog>) inv -> inv.getArgument(0, AiCallLog.class));
    }

    @Test
    @DisplayName("메뉴 생성 성공: LLM 응답을 그대로 사용하고 로그가 저장된다")
    void generateMenuDescription_success() {

        // [given]
        UUID userId = UUID.randomUUID();
        User owner = mock(User.class);
        when(userReader.findActiveUser(userId)).thenReturn(owner);

        String menuName = "불고기덮밥";
        String category = "한식";
        String features = "불고기, 양파, 간장소스";
        String outputText = "달큼한 간장 소스에 볶아낸 불고기를 따끈한 밥 위에 올린 메뉴입니다.";

        when(chatClient.prompt().user(anyString()).call().content()).thenReturn(outputText);

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
    @DisplayName("메뉴 생성 예외 발생 시 fallback 문구로 저장된다")
    void generateMenuDescription_fallback_onException() {

        // [given]
        UUID userId = UUID.randomUUID();
        User owner = mock(User.class);
        when(userReader.findActiveUser(userId)).thenReturn(owner);

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
