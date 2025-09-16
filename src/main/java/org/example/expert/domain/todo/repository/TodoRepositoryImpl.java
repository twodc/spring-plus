package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSimpleResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public Page<TodoSimpleResponse> findByQuery(Pageable pageable, String title, LocalDate startDate, LocalDate endDate, String nickname) {

        QTodo todo = QTodo.todo;
        QManager manager = QManager.manager;
        QUser user = QUser.user;
        QComment comment = QComment.comment;

        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(title)) {
            builder.and(todo.title.contains(title));
        }
        if (startDate != null && endDate != null) {
            builder.and(todo.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
        }
        if (StringUtils.hasText(nickname)) {
            builder.and(user.nickname.contains(nickname));
        }

        List<TodoSimpleResponse> todos = jpaQueryFactory
                .select(Projections.constructor(
                        TodoSimpleResponse.class,
                        todo.id,
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct()
                ))
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(comment).on(comment.todo.eq(todo))
                .leftJoin(manager.user, user)
                .where(builder)
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(builder);

        return PageableExecutionUtils.getPage(todos, pageable, countQuery::fetchOne);
    }
}
