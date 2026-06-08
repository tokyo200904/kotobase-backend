package kotobase_backend.modules.progress.service.impl;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.comom.enums.KanjiType;
import kotobase_backend.comom.enums.LearningStatus;
import kotobase_backend.comom.enums.Level;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import kotobase_backend.modules.examples.mapper.ExampleMapper;
import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiReadingResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.payment.service.PremiumGuardService;
import kotobase_backend.modules.progress.dto.response.StationItemsResponse;
import kotobase_backend.modules.progress.dto.response.StationResponse;
import kotobase_backend.modules.progress.dto.response.SubmitTestRequest;
import kotobase_backend.modules.progress.entity.Station;
import kotobase_backend.modules.progress.entity.StationItem;
import kotobase_backend.modules.progress.entity.UserItemProgress;
import kotobase_backend.modules.progress.entity.UserLevelProgress;
import kotobase_backend.modules.progress.repository.StationRepository;
import kotobase_backend.modules.progress.repository.UserItemProgressRepository;
import kotobase_backend.modules.progress.repository.UserLevelProgressRepository;
import kotobase_backend.modules.progress.service.RoadmapService;
import kotobase_backend.modules.progress.service.UserItemProgressService;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.UserRepository;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {

    private final StationRepository stationRepository;
    private final UserLevelProgressRepository progressRepository;
    private final UserItemProgressRepository userItemProgressRepository;
    private final UserRepository userRepository;
    private final KanjiRepository kanjiRepository;
    private final VocabRepository vocabRepository;
    private final UserItemProgressService  userItemProgressService;
    private final ExampleMapper exampleMapper;
    private final PremiumGuardService premiumGuardService;

    @Override
    public List<StationResponse> getRoadmap(Integer userId, Integer levelId, ItemType itemType) {

        boolean isPremium = (userId != null) && premiumGuardService.check(userId);

        List<Station> stations = stationRepository.findByLevelIdAndItemTypeOrderByStationOrderAsc(levelId, itemType);

        Integer highestUnlocked = 1;
        if (userId != null) {
            highestUnlocked = progressRepository
                    .findByUserIdAndLevelIdAndItemType(userId, levelId, itemType)
                    .map(UserLevelProgress::getHighestUnlockedStation)
                    .orElse(1);
        }

        final Integer finalHighestUnlocked = highestUnlocked;

        return stations.stream().map(station -> {
            String status;
            int order = station.getStationOrder();

            if (!isPremium && station.getLevel().getLevel() != Level.N5) {
                status = "LOCKED";
            } else {
                if (order < finalHighestUnlocked) {
                    status = "COMPLETED";
                } else if (order == finalHighestUnlocked) {
                    status = "UNLOCKED";
                } else {
                    status = "LOCKED";
                }
            }

            return StationResponse.builder()
                    .id(station.getId())
                    .stationOrder(order)
                    .title(station.getTitle())
                    .description(station.getDescription())
                    .totalItems(station.getStationItems() != null ? station.getStationItems().size() : 0)
                    .status(status)
                    .build();
        }).toList();
    }

    @Override
    @Transactional
    public void completeStation(Integer userId, Integer stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạm học"));

        if (station.getLevel().getLevel() != Level.N5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để thực hiện.");
            }
            premiumGuardService.enforcePremium(userId, "Lộ trình học cấp độ " + station.getLevel().getLevel() + " yêu cầu tài khoản Premium.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        UserLevelProgress progress = progressRepository
                .findByUserIdAndLevelIdAndItemType(userId, station.getLevel().getId(), station.getItemType())
                .orElseGet(() -> UserLevelProgress.builder()
                        .userId(userId)
                        .level(station.getLevel())
                        .itemType(station.getItemType())
                        .highestUnlockedStation(1)
                        .build());

        if (station.getStationOrder() > progress.getHighestUnlockedStation()) {
            throw new RuntimeException("Trạm này chưa được mở khóa.");
        }

        if (station.getStationOrder() == progress.getHighestUnlockedStation()) {
            progress.setHighestUnlockedStation(station.getStationOrder() + 1);
            progressRepository.save(progress);
        }

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        for (StationItem item : station.getStationItems()) {
            boolean exists = userItemProgressRepository.existsByUserIdAndItemTypeAndItemId(
                    userId, station.getItemType(), item.getTargetItemId()
            );

            if (!exists) {
                UserItemProgress newItem = UserItemProgress.builder()
                        .user(user)
                        .itemId(item.getTargetItemId())
                        .itemType(station.getItemType())
                        .status(LearningStatus.LEARNING)
                        .memoryLevel(1)
                        .nextReviewDate(tomorrow)
                        .build();
                userItemProgressRepository.save(newItem);
            }
        }
    }

    @Override
    public StationItemsResponse getStationItems(Integer stationId, Integer userId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạm học tương ứng"));

        System.out.println("\n=== DEBUG BẢO MẬT TRẠM HỌC ===");
        System.out.println("1. Người dùng đang bấm vào Trạm ID: " + stationId);
        System.out.println("2. Spring Boot nhận diện Level của Trạm này là: " + station.getLevel().getLevel());
        System.out.println("3. ID của User đang gửi Request: " + userId);

        if (userId != null) {
            boolean isVip = premiumGuardService.check(userId);
            System.out.println("4. Kết quả check VIP từ DB cho User này là: " + isVip);
        }
        System.out.println("================================\n");

        if (station.getLevel().getLevel() != Level.N5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để học nội dung này.");
            }
            premiumGuardService.enforcePremium(userId, "Nội dung trạm học thuộc trình độ " + station.getLevel().getLevel() + " yêu cầu Premium.");
        }

        List<StationItem> stationItems = station.getStationItems();

        if (stationItems == null || stationItems.isEmpty()) {
            return new StationItemsResponse(
                    station.getItemType(),
                    Collections.emptyList()
            );
        }

        List<Integer> targetIds = stationItems.stream()
                .map(StationItem::getTargetItemId)
                .toList();

        if (station.getItemType() == ItemType.KANJI) {

            List<Kanji> kanjis = kanjiRepository.findAllById(targetIds);

            List<KanjiDetelResponse> items = kanjis.stream()
                    .map(kanji -> {

                        List<KanjiReadingResponse> onKanji =
                                kanji.getReadings().stream()
                                        .filter(r -> r.getKanjiType() == KanjiType.ON)
                                        .map(r -> new KanjiReadingResponse(
                                                r.getReading(),
                                                r.getRomaji()))
                                        .toList();

                        List<KanjiReadingResponse> kunKanji =
                                kanji.getReadings().stream()
                                        .filter(r -> r.getKanjiType() == KanjiType.KUN)
                                        .map(r -> new KanjiReadingResponse(
                                                r.getReading(),
                                                r.getRomaji()))
                                        .toList();

                        List<ExampleResponse> examples =
                                kanji.getExampleKanjis() == null
                                        ? List.of()
                                        : kanji.getExampleKanjis()
                                        .stream()
                                        .map(exampleMapper::toExampleResponseKanji)
                                        .toList();

                        return new KanjiDetelResponse(
                                kanji.getId(),
                                kanji.getCharacters(),
                                kanji.getMeaning(),
                                kanji.getStrokeCount(),
                                onKanji,
                                kunKanji,
                                kanji.getLevel().getLevel(),
                                kanji.getHan(),
                                examples
                        );
                    })
                    .toList();

            return new StationItemsResponse(
                    ItemType.KANJI,
                    items
            );
        }

        List<Vocab> vocabs = vocabRepository.findAllById(targetIds);

        List<VocabResponse> items = vocabs.stream()
                .map(vocab -> {

                    VocabResponse response = new VocabResponse();

                    response.setId(vocab.getId());
                    response.setWord(vocab.getWord());
                    response.setReading(vocab.getReading());
                    response.setMeaning(vocab.getMeaning());
                    response.setRomaji(vocab.getRomaji());

                    List<ExampleResponse> examples =
                            vocab.getExampleVocabs() == null
                                    ? List.of()
                                    : vocab.getExampleVocabs()
                                    .stream()
                                    .map(exampleMapper::toExampleResponse)
                                    .toList();

                    response.setExamples(examples);

                    return response;
                })
                .toList();

        return new StationItemsResponse(
                ItemType.VOCAB,
                items
        );
    }

    @Override
    @Transactional
    public Map<String, Object> submitAndEvaluateTest(Integer userId, Integer stationId, SubmitTestRequest request) {

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạm"));

        if (station.getLevel().getLevel() != Level.N5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để làm bài kiểm tra.");
            }
            premiumGuardService.enforcePremium(userId, "Bài kiểm tra lên trạm trình độ " + station.getLevel().getLevel() + " yêu cầu Premium.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(" khong tim thay user "));

        int correctCount = 0;
        int timePerQuestion = request.getTimeSpentSeconds() / Math.max(1, request.getAnswers().size());

        for (SubmitTestRequest.AnswerRequest ans : request.getAnswers()) {

            boolean isAlreadyLearned = userItemProgressRepository.existsByUserIdAndItemTypeAndItemId(
                    userId, station.getItemType(), ans.getQuestionItemId()
            );

            boolean isFirstReviewToday = !isAlreadyLearned;

            if (ans.getQuestionItemId().equals(ans.getSelectedItemId())) {
                correctCount++;
                userItemProgressService.updateDailyStats(userId, true, timePerQuestion, isFirstReviewToday);
            } else {
                userItemProgressService.updateDailyStats(userId, false, timePerQuestion, isFirstReviewToday);
            }
        }

        int totalQuestions = station.getStationItems().size();
        double percentage = (double) correctCount / totalQuestions * 100;

        Map<String, Object> response = new HashMap<>();
        response.put("score", percentage);
        response.put("correctCount", correctCount);
        response.put("totalQuestions", totalQuestions);

        if (percentage >= 80.0) {
            response.put("isPassed", true);
            response.put("message", "Chúc mừng! Bạn đã qua ải.");

            UserLevelProgress progress = progressRepository
                    .findByUserIdAndLevelIdAndItemType(userId, station.getLevel().getId(), station.getItemType())
                    .orElseGet(() -> UserLevelProgress.builder()
                            .userId(userId).level(station.getLevel()).itemType(station.getItemType()).highestUnlockedStation(1)
                            .build());

            if (station.getStationOrder() == progress.getHighestUnlockedStation()) {
                progress.setHighestUnlockedStation(station.getStationOrder() + 1);
                progressRepository.save(progress);
            }

            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            for (StationItem item : station.getStationItems()) {

                if (!userItemProgressRepository.existsByUserIdAndItemTypeAndItemId(userId, station.getItemType(), item.getTargetItemId())) {
                    userItemProgressRepository.save(UserItemProgress.builder()
                            .user(user)
                            .itemId(item.getTargetItemId())
                            .itemType(station.getItemType())
                            .status(LearningStatus.LEARNING)
                            .memoryLevel(1)
                            .nextReviewDate(tomorrow)
                            .build());
                }
            }
        } else {
            response.put("isPassed", false);
            response.put("message", "Bạn chưa đủ 80% để qua ải. Hãy học lại nhé!");
        }

        return response;
    }

}
