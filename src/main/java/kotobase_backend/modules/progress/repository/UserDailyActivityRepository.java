package kotobase_backend.modules.progress.repository;

import kotobase_backend.modules.progress.entity.UserDailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDailyActivityRepository extends JpaRepository<UserDailyActivity, Integer> {
    Optional<UserDailyActivity> findByUserIdAndStudyDate(Integer userId, LocalDate studyDate);

    @Query("SELECT u.studyDate FROM " +
            "UserDailyActivity u WHERE u.user.id = :userId " +
            "AND u.totalReviewed > 0 " +
            "ORDER BY u.studyDate DESC")
    List<LocalDate> findActiveStudyDates(@Param("userId") Integer userId);
}