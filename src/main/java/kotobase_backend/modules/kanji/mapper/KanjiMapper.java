package kotobase_backend.modules.kanji.mapper;

import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import kotobase_backend.modules.examples.mapper.ExampleMapper;
import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiFindResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiReadingResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KanjiMapper {
    private final ExampleMapper exampleMapper;

    public KanjisResponse toKanjiResponse(Kanji kanji,boolean isLocked) {
        KanjisResponse kanjisResponse = new KanjisResponse();
        kanjisResponse.setId(kanji.getId());
        kanjisResponse.setCharacters(kanji.getCharacters());
        kanjisResponse.setIsLocked(isLocked);
        return kanjisResponse;
    }

    public KanjiDetelResponse toDetelResponse(Kanji kanji, List<KanjiReadingResponse> onKanjiReadings, List<KanjiReadingResponse> kunKanjiReadings) {
        List<ExampleResponse> examples =
                kanji.getExampleKanjis() == null ? List.of() : kanji.getExampleKanjis().stream()
                        .map(exampleMapper::toExampleResponseKanji)
                        .toList();
        KanjiDetelResponse kanjiDetelResponse = new KanjiDetelResponse();
        kanjiDetelResponse.setId(kanji.getId());
        kanjiDetelResponse.setCharacters(kanji.getCharacters());
        kanjiDetelResponse.setMeaning(kanji.getMeaning());
        kanjiDetelResponse.setStrokeCount(kanji.getStrokeCount());
        kanjiDetelResponse.setOn(onKanjiReadings);
        kanjiDetelResponse.setKun(kunKanjiReadings);
        kanjiDetelResponse.setLevel(kanji.getLevel().getLevel());
        kanjiDetelResponse.setHan(kanji.getHan());
        kanjiDetelResponse.setExamples(examples);
        return kanjiDetelResponse;
    }


    public KanjiFindResponse toFindResponse(Kanji kanji,boolean isLocked) {
        KanjiFindResponse kanjiFindResponse = new KanjiFindResponse();
        kanjiFindResponse.setId(kanji.getId());
        kanjiFindResponse.setCharacters(kanji.getCharacters());
        kanjiFindResponse.setMeaning(kanji.getMeaning());
        kanjiFindResponse.setHan(kanji.getHan());
        kanjiFindResponse.setIsLocked(isLocked);
        return kanjiFindResponse;
    }
}
