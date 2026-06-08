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
import kotobase_backend.modules.payment.service.PremiumGuardService;
import kotobase_backend.modules.progress.repository.UserItemProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class kanjiServiceImpl implements KanjiService {

    private final KanjiRepository kanjiRepository;
    private final KanjiMapper kanjiMapper;
    private final UserItemProgressRepository userItemProgressRepository;
    private final PremiumGuardService premiumGuardService;

    @Override
    public List<KanjisResponse> getKanji(Level level,Integer userId) {
        if (level != Level.N5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để xem Kanji " + level.name());
            }
            premiumGuardService.enforcePremium(userId, "Hãy nâng cấp Premium để học danh sách Kanji " + level.name());
        }

        List<Kanji> kanjiList = kanjiRepository.findByLevel(level);

        return kanjiList.stream()
                .map(kanji -> {
                    return kanjiMapper.toKanjiResponse(kanji, false);
                })
                .toList();
    }

    @Override
    public KanjiDetelResponse getKanjiDetel(Integer id, Integer userId) {
        Kanji kanji = kanjiRepository.getKanjiByID(id)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy kanji"));

        if (kanji.getLevel().getLevel() != Level.N5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để xem chi tiết Kanji này.");
            }
            premiumGuardService.enforcePremium(userId, "Hãy nâng cấp Premium để học ");
        }
        List<KanjiReadingResponse> onKanji = kanji.getReadings().stream()
                .filter(r -> r.getKanjiType() == KanjiType.ON)
                .map(r -> new KanjiReadingResponse(r.getReading(), r.getRomaji()))
                .toList();

        List<KanjiReadingResponse> kunKanji = kanji.getReadings().stream()
                .filter(k ->k.getKanjiType() == KanjiType.KUN)
                .map(o -> new KanjiReadingResponse(o.getReading(),o.getRomaji()))
                .toList();

        return kanjiMapper.toDetelResponse(kanji, onKanji, kunKanji);
    }

    @Override
    public List<KanjiFindResponse> findKanji(String key, Integer userId) {
        List<Kanji> kanjiList = kanjiRepository.findKanji(key);
        if (kanjiList.isEmpty()){
            return Collections.emptyList();
        }
        boolean isPremium = (userId != null) && premiumGuardService.check(userId);

        return kanjiList.stream()
                .map(kanji -> {
                    boolean isLocked = !isPremium && (kanji.getLevel().getLevel() != Level.N5);
                    return kanjiMapper.toFindResponse(kanji, isLocked);
                })
                .toList();
    }


}
