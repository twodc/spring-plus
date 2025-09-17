package org.example.expert.domain.log;

import jakarta.transaction.Transactional;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.enums.ActionType;
import org.example.expert.domain.log.repository.LogRepository;
import org.example.expert.domain.log.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LogServiceTest {

    @Autowired
    private LogService logService;

    @Autowired
    private LogRepository logRepository;

    @Test
    void 로그_성공_저장() {

        // given
        Log log = new Log(
                1L,
                100L,
                200L,
                ActionType.SUCCESS,
                "담당자 등록 성공"
        );

        // when
        logService.saveLog(log);

        // then
        List<Log> logs = logRepository.findAll();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getType()).isEqualTo(ActionType.SUCCESS);
        assertThat(logs.get(0).getMessage()).contains("성공");
    }

    @Test
    void 로그_실패_저장() {

        // given
        Log log = new Log(
                2L,
                101L,
                101L,
                ActionType.FAILURE,
                "담당자 등록 실패: 일정 작성자는 본인을 지정할 수 없습니다."
        );

        // when
        logService.saveLog(log);

        // then
        Log savedLog = logRepository.findById(log.getId()).orElseThrow();
        assertThat(savedLog.getType()).isEqualTo(ActionType.FAILURE);
        assertThat(savedLog.getMessage()).contains("실패");
    }
}
