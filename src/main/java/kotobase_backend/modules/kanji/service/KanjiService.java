package kotobase_backend.modules.kanji.service;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;

import java.util.List;

public interface KanjiService {
    public List<KanjisResponse> getKanji(Level level);
    public KanjiDetelResponse getKanjiDetel(Integer id);
}
