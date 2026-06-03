package kotobase_backend.modules.progress.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class DashboardStatsResponse {
    private int totalReviewedToday;
    private int accuracyPercentage;
    private int currentStreak;
    private int minutesSpentToday;
    private Map<String, Long> statusDistribution;
    private int kanjiDueToday;
    private int vocabDueToday;
}