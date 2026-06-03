package kotobase_backend.modules.progress.service.impl;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.dto.response.DashboardStatsResponse;
import kotobase_backend.modules.progress.entity.UserDailyActivity;
import kotobase_backend.modules.progress.repository.UserDailyActivityRepository;
import kotobase_backend.modules.progress.repository.UserItemProgressRepository;
import kotobase_backend.modules.progress.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserDailyActivityRepository dailyActivityRepository;
    private final UserItemProgressRepository userItemProgressRepository;

    @Override
    public DashboardStatsResponse getDashboardStats(Integer userId) {
        LocalDate today = LocalDate.now();
        UserDailyActivity todayStat = dailyActivityRepository.findByUserIdAndStudyDate(userId, today).orElse(null);

        int total = 0, accuracy = 0, studyMinutes = 0;

        if (todayStat != null && todayStat.getTotalReviewed() > 0) {
            total = todayStat.getTotalReviewed();
            accuracy = (int) Math.round(((double) todayStat.getCorrectAnswers() / total) * 100);
            studyMinutes = todayStat.getSecondsSpent() / 60;
        }

        List<Object[]> statusCounts = userItemProgressRepository.countItemsByStatus(userId);
        Map<String, Long> distribution = new HashMap<>();

        distribution.put("NEW", 0L);
        distribution.put("LEARNING", 0L);
        distribution.put("MASTERED", 0L);

        for (Object[] row : statusCounts) {
            String statusName = row[0].toString();
            Long count = (Long) row[1];
            distribution.put(statusName, count);
        }

        int kanjiDue = userItemProgressRepository.countDueItemsByType(userId, ItemType.KANJI);
        int vocabDue = userItemProgressRepository.countDueItemsByType(userId, ItemType.VOCAB);

        return DashboardStatsResponse.builder()
                .totalReviewedToday(total)
                .accuracyPercentage(accuracy)
                .minutesSpentToday(studyMinutes)
                .currentStreak(calculateStreak(userId, today))
                .statusDistribution(distribution)
                .kanjiDueToday(kanjiDue)
                .vocabDueToday(vocabDue)
                .build();
    }

    private int calculateStreak(Integer userId, LocalDate today) {
        List<LocalDate> dates = dailyActivityRepository.findActiveStudyDates(userId);
        if (dates.isEmpty()) return 0;

        LocalDate yesterday = today.minusDays(1);
        if (!dates.contains(today) && !dates.contains(yesterday)) return 0;

        int streak = 0;
        LocalDate pointer = dates.contains(today) ? today : yesterday;

        for (LocalDate date : dates) {
            if (date.equals(pointer)) {
                streak++;
                pointer = pointer.minusDays(1);
            } else if (date.isBefore(pointer)) {
                break;
            }
        }
        return streak;
    }
}
