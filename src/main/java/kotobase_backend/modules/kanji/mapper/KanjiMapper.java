package kotobase_backend.modules.kanji.mapper;

import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiReadingResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KanjiMapper {
    public KanjisResponse toKanjiResponse(Kanji kanji) {
        KanjisResponse kanjisResponse = new KanjisResponse();
        kanjisResponse.setId(kanji.getId());
        kanjisResponse.setCharacters(kanji.getCharacters());
        return kanjisResponse;
    }

    public KanjiDetelResponse toDetelResponse(Kanji kanji, List<KanjiReadingResponse> onKanjiReadings, List<KanjiReadingResponse> kunKanjiReadings) {
        KanjiDetelResponse kanjiDetelResponse = new KanjiDetelResponse();
        kanjiDetelResponse.setId(kanji.getId());
        kanjiDetelResponse.setCharacters(kanji.getCharacters());
        kanjiDetelResponse.setMeaning(kanji.getMeaning());
        kanjiDetelResponse.setStrokeCount(kanji.getStrokeCount());
        kanjiDetelResponse.setOn(onKanjiReadings);
        kanjiDetelResponse.setKun(kunKanjiReadings);
        return kanjiDetelResponse;
    }
}
