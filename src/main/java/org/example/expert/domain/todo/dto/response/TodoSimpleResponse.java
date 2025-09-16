package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoSimpleResponse {

    private final Long id;
    private final String title;
    private final Long totalManagers;
    private final Long totalComments;

    public TodoSimpleResponse(Long id, String title, Long totalManagers, Long totalComments) {
        this.id = id;
        this.title = title;
        this.totalManagers = totalManagers;
        this.totalComments = totalComments;
    }
}
