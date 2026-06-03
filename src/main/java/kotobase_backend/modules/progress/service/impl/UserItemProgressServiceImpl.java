package kotobase_backend.modules.progress.service.impl;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.comom.enums.LearningStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.grammar.repository.GrammarRepository;
import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.progress.dto.request.ItemRequest;
import kotobase_backend.modules.progress.dto.request.SubmitReviewRequest;
import kotobase_backend.modules.progress.dto.response.QuizQuestionResponse;
import kotobase_backend.modules.progress.entity.UserDailyActivity;
import kotobase_backend.modules.progress.entity.UserItemProgress;
import kotobase_backend.modules.progress.repository.UserDailyActivityRepository;
import kotobase_backend.modules.progress.repository.UserItemProgressRepository;
import kotobase_backend.modules.progress.service.UserItemProgressService;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.UserRepository;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserItemProgressServiceImpl implements UserItemProgressService {

    private final UserItemProgressRepository userItemProgressRepository;
    private final UserRepository userRepository;
    private final KanjiRepository kanjiRepository;
    private final GrammarRepository grammarRepository;
    private final VocabRepository vocabRepository;
    private final UserDailyActivityRepository userDailyActivityRepository;

    @Override
    @Transactional
    public boolean addOrUnAdd(Integer userId, ItemRequest itemRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy user"));
        boolean isItem = false;
        switch (itemRequest.getItemType()) {
            case KANJI:
                isItem = kanjiRepository.existsById(itemRequest.getItemId());
                if (!isItem) throw new ResourceNotFoundException("kanji không tồn tại");
                break;
            case VOCAB:
                isItem = vocabRepository.existsById(itemRequest.getItemId());
                if (!isItem) throw new ResourceNotFoundException("từ vựng không tồn tại");
                break;
            default:
                throw new ResourceNotFoundException("dữ liệu không hợp lệ");

        }
        Optional<UserItemProgress> item = userItemProgressRepository
                .findByUser_IdAndItemIdAndItemType(userId, itemRequest.getItemId(), itemRequest.getItemType());
        if (item.isPresent()) {
            userItemProgressRepository.delete(item.get());
            return false;
        }
        else {
            UserItemProgress userItemProgress = UserItemProgress.builder()
                    .user(user)
                    .itemId(itemRequest.getItemId())
                    .itemType(itemRequest.getItemType())
                    .memoryLevel(1)
                    .status(LearningStatus.NEW)
                    .nextReviewDate(LocalDateTime.now())
                    .build();
            userItemProgressRepository.save(userItemProgress);
            return true;
        }
    }

    @Override
    public QuizQuestionResponse getNextQuestion(Integer userId, ItemType type) {

        List<UserItemProgress> dueItems = userItemProgressRepository.findDueItemsByType(userId, type);

        if (dueItems.isEmpty()) {
            throw new RuntimeException("bạn không còn thẻ nào cần ôn hôm nay.");
        }

        UserItemProgress itemToReview = dueItems.get(0);

        List<QuizQuestionResponse.OptionDto> options = new ArrayList<>();
        String questionText = "";
        Integer correctOptionId = null;

        if (itemToReview.getItemType() == ItemType.KANJI) {
            Kanji correct = kanjiRepository.findById(itemToReview.getItemId()).orElseThrow();
            questionText = correct.getCharacters();
            correctOptionId = correct.getId();
            options.add(new QuizQuestionResponse.OptionDto((correct.getId()),correct.getMeaning()));
            List<Kanji> distractors = kanjiRepository.findRandomDistractors(correct.getLevel().getId(),correct.getId());
            for (Kanji k : distractors) {
                options.add(new QuizQuestionResponse.OptionDto(k.getId(),k.getMeaning()));
            }
        }
        else if (itemToReview.getItemType() == ItemType.VOCAB) {
            Vocab correct = vocabRepository.findById(itemToReview.getItemId()).orElseThrow();
            questionText = (correct.getWord() != null && !correct.getWord().isEmpty())
                    ? correct.getWord()
                    : correct.getReading();
            correctOptionId = correct.getId();

            options.add(new QuizQuestionResponse.OptionDto(correct.getId(), correct.getMeaning()));

            List<Vocab> distractors = vocabRepository.findRandomDistractors(correct.getLevel().getId(), correct.getId());
            for (Vocab v : distractors) {
                options.add(new QuizQuestionResponse.OptionDto(v.getId(), v.getMeaning()));
            }
        }

        Collections.shuffle(options);

        return QuizQuestionResponse.builder()
                .progressId(itemToReview.getId())
                .targetItemId(itemToReview.getItemId())
                .questionText(questionText)
                .itemType(itemToReview.getItemType().name())
                .options(options)
                .build();
    }

    @Override
    @Transactional
    public void submitAnswer(Integer userId, SubmitReviewRequest request) {
        UserItemProgress progress = userItemProgressRepository.findById(request.getProgressId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dữ liệu tiến trình"));

        LocalDateTime now = LocalDateTime.now();

        boolean isFirstReviewToday = true;
        if (progress.getLastReviewedAt() != null) {
            isFirstReviewToday = !progress.getLastReviewedAt().toLocalDate().isEqual(now.toLocalDate());
        }

        progress.setLastReviewedAt(now);

        if (request.getIsCorrect()) {
            int newLevel = Math.min(progress.getMemoryLevel() + 1, 5);
            progress.setMemoryLevel(newLevel);
            progress.setStatus(newLevel == 5 ? LearningStatus.MASTERED : LearningStatus.LEARNING);
            progress.setNextReviewDate(calculateSrsNextDate(newLevel, now));
        } else {
            progress.setMemoryLevel(1);
            progress.setStatus(LearningStatus.LEARNING);
            progress.setNextReviewDate(now.plusDays(1));
        }
        userItemProgressRepository.save(progress);
        updateDailyStats(userId, request.getIsCorrect(), request.getTimeSpentSeconds(),isFirstReviewToday);
    }

    @Override
    public List<QuizQuestionResponse> getExtraPracticeQuestion(Integer userId, ItemType type) {

        List<UserItemProgress> randomItems = userItemProgressRepository.getRandomItemForPractice(userId, type.name());

        if (randomItems.isEmpty()) {
            throw new RuntimeException("Bạn chưa lưu từ nào. Hãy thêm từ vựng trước khi luyện tập nhé!");
        }

        List<QuizQuestionResponse> quizList = new ArrayList<>();

        for (UserItemProgress item : randomItems) {
            List<QuizQuestionResponse.OptionDto> options = new ArrayList<>();
            String questionText = "";
            Integer correctOptionId = null;

            if (item.getItemType() == ItemType.KANJI) {
                Kanji correct = kanjiRepository.findById(item.getItemId()).orElseThrow();
                questionText = correct.getCharacters();
                correctOptionId = correct.getId();
                options.add(new QuizQuestionResponse.OptionDto(correct.getId(), correct.getMeaning()));

                List<Kanji> distractors = kanjiRepository.findRandomDistractors(correct.getLevel().getId(), correct.getId());
                for (Kanji k : distractors) {
                    options.add(new QuizQuestionResponse.OptionDto(k.getId(), k.getMeaning()));
                }
            }

            else if (item.getItemType() == ItemType.VOCAB) {
                Vocab correct = vocabRepository.findById(item.getItemId()).orElseThrow();

                questionText = (correct.getWord() != null && !correct.getWord().isEmpty())
                        ? correct.getWord()
                        : correct.getReading();
                correctOptionId = correct.getId();
                options.add(new QuizQuestionResponse.OptionDto(correct.getId(), correct.getMeaning()));

                List<Vocab> distractors = vocabRepository.findRandomDistractors(correct.getLevel().getId(), correct.getId());

                for (Vocab v : distractors) {
                    options.add(new QuizQuestionResponse.OptionDto(v.getId(), v.getMeaning()));
                }
            }

            Collections.shuffle(options);

            quizList.add(QuizQuestionResponse.builder()
                    .progressId(item.getId())
                    .targetItemId(item.getItemId())
                    .questionText(questionText)
                    .itemType(item.getItemType().name())
                    .options(options)
                    .correctId(correctOptionId)
                    .build());
        }
        return quizList;
    }


    private LocalDateTime calculateSrsNextDate(int level, LocalDateTime time) {
        return switch (level) {
            case 1 -> time.plusDays(1);
            case 2 -> time.plusDays(3);
            case 3 -> time.plusDays(7);
            case 4 -> time.plusDays(14);
            case 5 -> time.plusDays(30);
            default -> time.plusDays(1);
        };
    }

    private void updateDailyStats(Integer userId, boolean isCorrect, Integer timeSpentSeconds,boolean isFirstReviewToday) {
        LocalDate today = LocalDate.now();

        User users = userRepository.findById(userId).orElseThrow();

        UserDailyActivity stat = userDailyActivityRepository.findByUserIdAndStudyDate(userId, today)
                .orElse(UserDailyActivity
                        .builder()
                        .user(users)
                        .studyDate(today)
                        .totalReviewed(0)
                        .correctAnswers(0)
                        .secondsSpent(0)
                        .build());

        if (isFirstReviewToday) {
            stat.setTotalReviewed(stat.getTotalReviewed() + 1);
            if (isCorrect) {
                stat.setCorrectAnswers(stat.getCorrectAnswers() + 1);
            }
        }

        int timeToAdd = (timeSpentSeconds != null) ? timeSpentSeconds : 0;
        int currentSeconds = (stat.getSecondsSpent() != null) ? stat.getSecondsSpent() : 0;
        stat.setSecondsSpent(currentSeconds + timeToAdd);

        userDailyActivityRepository.save(stat);
    }

    @Transactional
    public void submitExtraPracticeAnswer(Integer userId, SubmitReviewRequest request) {
        UserItemProgress progress = userItemProgressRepository.findById(request.getProgressId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiến trình học"));

        LocalDateTime now = LocalDateTime.now();

        boolean isFirstReviewToday = true;
        if (progress.getLastReviewedAt() != null) {
            isFirstReviewToday = !progress.getLastReviewedAt().toLocalDate().isEqual(now.toLocalDate());
        }

        progress.setLastReviewedAt(now);

        updateDailyStats(userId, request.getIsCorrect(), request.getTimeSpentSeconds(), isFirstReviewToday);

        if (progress.getStatus() == LearningStatus.NEW) {
            progress.setStatus(LearningStatus.LEARNING);
            userItemProgressRepository.save(progress);
        }
    }
}

