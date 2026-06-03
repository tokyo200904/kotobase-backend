package kotobase_backend.modules.kanji.service.impl;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.comom.enums.KanjiType;
import kotobase_backend.comom.enums.Level;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiFindResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiReadingResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.kanji.mapper.KanjiMapper;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.kanji.service.KanjiService;
import kotobase_backend.modules.progress.repository.UserItemProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class kanjiServiceImpl implements KanjiService {

    private final KanjiRepository kanjiRepository;
    private final KanjiMapper kanjiMapper;
    private final UserItemProgressRepository userItemProgressRepository;

    @Override
    public List<KanjisResponse> getKanji(Level level) {
        List<Kanji> kanji = kanjiRepository.findByLevel(level);
        return kanji.stream()
                .map(kanjiMapper::toKanjiResponse)
                .toList();
    }

    @Override
    public KanjiDetelResponse getKanjiDetel(Integer id, Integer userId) {
        Kanji kanji = kanjiRepository.getKanjiByID(id)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy kanji "));

        List<KanjiReadingResponse> onKanji = kanji.getReadings().stream()
                .filter(r -> r.getKanjiType() == KanjiType.ON)
                .map(r -> new KanjiReadingResponse(r.getReading(), r.getRomaji()))
                .toList();

        List<KanjiReadingResponse> kunKanji = kanji.getReadings().stream()
                .filter(k ->k.getKanjiType() == KanjiType.KUN)
                .map(o -> new KanjiReadingResponse(o.getReading(),o.getRomaji()))
                .toList();

        boolean isSaved = false;
        if (userId != null) {
            isSaved = userItemProgressRepository.existsByUserIdAndItemIdAndItemType(userId, id, ItemType.KANJI);
        }

        return kanjiMapper.toDetelResponse(kanji, onKanji, kunKanji, isSaved);
    }

    @Override
    public List<KanjiFindResponse> findKanji(String key) {
        List<Kanji> kanji = kanjiRepository.findKanji(key);
        if (kanji.isEmpty()){
            return Collections.emptyList();
        }
        return kanji.stream()
                .map(kanjiMapper::toFindResponse)
                .toList();
    }


}
