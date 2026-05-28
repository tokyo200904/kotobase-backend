package kotobase_backend.modules.exam.service.scoring;

import jakarta.transaction.Transactional;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.entity.*;
import kotobase_backend.modules.exam.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringQueueService {

    private final ScoringExecutorService scoringExecutorService;


    @Async("scoringTaskExecutor")
    public void calculateScoreBackground(Long attemptId) {
        try {
            scoringExecutorService.doCalculateScore(attemptId);
        } catch (Exception e) {
            log.error("Scoring Queue Lỗi khi chấm Attempt ID: {}", attemptId, e);
        }
    }

}
