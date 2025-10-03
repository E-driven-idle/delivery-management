package com.driven.dm.ai.domain.entity;

import com.driven.dm.global.entity.HistoryBaseEntity;
import com.driven.dm.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = {"user", "prompt", "outputText"})
@Table(name = "p_ai_call_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiCallLog extends HistoryBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ai_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ai_provider")
    private String aiProvider;

    @Column(name = "model")
    private String model;

    @Column(name = "prompt", columnDefinition = "text")
    private String prompt;

    @Column(name = "output_text", columnDefinition = "text")
    private String outputText;

    protected AiCallLog(
        User user, String aiProvider, String model, String prompt, String outputText) {

        this.user = user;
        this.aiProvider = aiProvider;
        this.model = model;
        this.prompt = prompt;
        this.outputText = outputText;
    }

    public static AiCallLog of(
        User user, String aiProvider, String model, String prompt, String outputText) {

        return new AiCallLog(user, aiProvider, model, prompt, outputText);
    }
}
