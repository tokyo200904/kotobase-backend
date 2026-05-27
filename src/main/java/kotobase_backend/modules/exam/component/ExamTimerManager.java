package kotobase_backend.modules.exam.component;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class ExamTimerManager {
    private final ThreadPoolTaskScheduler scheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public ExamTimerManager() {
        this.scheduler = new ThreadPoolTaskScheduler();
        this.scheduler.setThreadNamePrefix("ExamBomber-");
        this.scheduler.setPoolSize(100);
        this.scheduler.initialize();
    }

    public void scheduleForceSubmit(Long attemptId, Long sectionId, Instant exactEndTime, Runnable task) {
        String taskId = attemptId + "_SECTION_" + sectionId;
        ScheduledFuture<?> future = scheduler.schedule(task, exactEndTime);
        scheduledTasks.put(taskId, future);
    }

    public void cancelTimer(Long attemptId, Long sectionId) {
        String taskId = attemptId + "_SECTION_" + sectionId;
        cancelTaskByKey(taskId);
    }

    public void scheduleMasterTimer(Long attemptId, Instant exactEndTime, Runnable task) {
        String taskId = attemptId + "_MASTER";
        ScheduledFuture<?> future = scheduler.schedule(task, exactEndTime);
        scheduledTasks.put(taskId, future);
    }

    public void cancelMasterTimer(Long attemptId) {
        String taskId = attemptId + "_MASTER";
        cancelTaskByKey(taskId);
    }

    private void cancelTaskByKey(String taskId) {
        ScheduledFuture<?> future = scheduledTasks.remove(taskId);
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
    }
}

