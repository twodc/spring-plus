package org.example.expert.domain.todo.repository;

import org.example.expert.config.QueryDslConfig;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.dto.response.TodoSimpleResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Test
    void findByQuery_조건_기반_검색() {

        // given
        User user = new User("email", "password", "nickname", UserRole.ROLE_USER);
        userRepository.save(user);
        for (int i = 0; i < 15; i++) {
            Todo todo = new Todo((i + 1) + ". title", "contents", "weather", user);
            ReflectionTestUtils.setField(todo, "createdAt", LocalDateTime.now().minusHours(1));
            todoRepository.save(todo);

            Manager manager = new Manager(user, todo);
            managerRepository.save(manager);
        }

        // when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<TodoSimpleResponse> todos = todoRepository.findByQuery(
                pageable,
                "title",
                LocalDate.now().minusDays(1),
                LocalDate.now(),
                "nick"
        );

        // then
        assertThat(todos.getTotalElements()).isEqualTo(15);
        assertThat(todos.getTotalPages()).isEqualTo(2);
        assertThat(todos.getContent().get(0).getTitle()).isEqualTo("15. title");
        assertThat(todos.getContent())
                .allMatch(todo -> todo.getTitle().contains("title"));
    }
}
