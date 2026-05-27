package kotobase_backend.modules.progress.service.impl;

import kotobase_backend.comom.enums.LearningStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.grammar.repository.GrammarRepository;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.progress.dto.request.ItemRequest;
import kotobase_backend.modules.progress.entity.UserItemProgress;
import kotobase_backend.modules.progress.repository.UserItemProgressRepository;
import kotobase_backend.modules.progress.service.UserItemProgressService;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.UserRepository;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserItemProgressServiceImpl implements UserItemProgressService {

    private final UserItemProgressRepository userItemProgressRepository;
    private final UserRepository userRepository;
    private final KanjiRepository kanjiRepository;
    private final GrammarRepository grammarRepository;
    private final VocabRepository vocabRepository;

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
            case GRAMMAR:
                isItem = grammarRepository.existsById(itemRequest.getItemId());
                if (!isItem) throw new ResourceNotFoundException("grammar không tồn tại");
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
}
