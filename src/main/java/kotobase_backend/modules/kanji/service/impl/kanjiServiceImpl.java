package kotobase_backend.modules.kanji.service.impl;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.kanji.service.KanjiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class kanjiServiceImpl implements KanjiService {

    private final KanjiRepository kanjiRepository;

    @Override
    public List<KanjisResponse> getKanji(Level level) {
        List<KanjisResponse> kanjisResponse = kanjiRepository.findByLevel(level);
        return kanjisResponse.isEmpty() ? Collections.emptyList() : kanjisResponse;
    }
}
