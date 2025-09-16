package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSimpleResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TodoRepositoryCustom {

    Todo findByIdWithUser(Long todoId);

    Page<TodoSimpleResponse> findByQuery(
            Pageable pageable,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String nickname
    );
}
