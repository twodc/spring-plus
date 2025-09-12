package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;

import java.util.Optional;

public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public TodoRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Todo findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, QUser.user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne()
        ).orElseThrow(() -> new InvalidRequestException("Todo not found"));
    }
}
