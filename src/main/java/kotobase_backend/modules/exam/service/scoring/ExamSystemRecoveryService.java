package kotobase_backend.modules.exam.service.scoring;


import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.modules.exam.component.ExamTimerManager;
import kotobase_backend.modules.exam.entity.ExamAttemptSection;
import kotobase_backend.modules.exam.entity.ExamSection;
import kotobase_backend.modules.exam.repository.ExamAttemptSectionRepository;
import kotobase_backend.modules.exam.repository.ExamSectionRepository;
import kotobase_backend.modules.exam.service.ExamTransitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamSystemRecoveryService {

     private final ExamAttemptSectionRepository attemptSectionRepository;
     private final ExamSectionRepository sectionRepository;
     private final ExamTimerManager timerManager;
     private final ExamTransitionService transitionService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void recoverScheduledTasks() {
        System.out.println("[Recovery System] Bắt đầu rà soát và khôi phục các bài thi đang dang dở...");

        List<ExamAttemptSection> activeSections = attemptSectionRepository.findByStatus(StatusSection.in_progress);

        int recoveredCount = 0;
        int expiredCount = 0;
        Instant now = Instant.now();

        for (ExamAttemptSection section : activeSections) {
            if (section.getStartedAt() == null) continue;

            ExamSection masterSection = sectionRepository.findById(section.getSection().getId()).orElse(null);
            if (masterSection == null) continue;

            Instant exactEndTime = section.getStartedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .plusSeconds(masterSection.getDurationMinutes() * 60L);

            Runnable forceSubmitTask = () -> {
                transitionService.processSectionSubmit(section.getExamAttempt().getId(), section.getSection().getId(), true);
            };

            if (exactEndTime.isBefore(now)) {
                timerManager.scheduleForceSubmit(
                        section.getExamAttempt().getId(),
                        section.getSection().getId(),
                        now.plusSeconds(2),
                        forceSubmitTask
                );
                expiredCount++;
            } else {
                timerManager.scheduleForceSubmit(
                        section.getExamAttempt().getId(),
                        section.getSection().getId(),
                        exactEndTime,
                        forceSubmitTask
                );
                recoveredCount++;
            }
        }
        System.out.println(" [Recovery System] Đã khôi phục: " + recoveredCount + " bom hẹn giờ.");
        System.out.println(" [Recovery System] Đang xử lý: " + expiredCount + " bài thi đã quá hạn ngầm.");
    }
}
