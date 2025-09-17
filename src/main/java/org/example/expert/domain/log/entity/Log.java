package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.log.enums.ActionType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long todoId;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private Long managerUserId;

    @Enumerated(EnumType.STRING)
    private ActionType type;

    private String message;

    public Log(Long todoId, Long authorId, Long managerUserId, ActionType type, String message) {
        this.todoId = todoId;
        this.authorId = authorId;
        this.managerUserId = managerUserId;
        this.type = type;
        this.message = message;
    }
}
