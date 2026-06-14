package kotobase_backend.modules.kanji.mapper;

import kotobase_backend.comom.enums.KanjiType;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.examples.entity.ExampleKanji;
import kotobase_backend.modules.kanji.dto.Request.KanjiAdminRequest;
import kotobase_backend.modules.kanji.dto.Response.KanjiAdminResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.kanji.entity.KanjiReading;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KanjiAdminMapper {
    private final JlptLevelRepository levelRepository;

    public void updateEntityFromRequest(KanjiAdminRequest request, Kanji kanji) {
        kanji.setCharacters(request.getCharacters());
        kanji.setMeaning(request.getMeaning());
        kanji.setStrokeCount(request.getStrokeCount());
        kanji.setHan(request.getHan());

        JlptLevel level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Level"));
        kanji.setLevel(level);

        if (kanji.getReadings() != null) {
            kanji.getReadings().clear();
        } else {
            kanji.setReadings(new ArrayList<>());
        }

        if (request.getOnReadings() != null) {
            List<KanjiReading> onList = request.getOnReadings().stream().map(req -> {
                KanjiReading r = new KanjiReading();
                r.setReading(req.getReading());
                r.setRomaji(req.getRomaji());
                r.setKanjiType(KanjiType.ON);
                r.setKanji(kanji);
                return r;
            }).toList();
            kanji.getReadings().addAll(onList);
        }

        if (request.getKunReadings() != null) {
            List<KanjiReading> kunList = request.getKunReadings().stream().map(req -> {
                KanjiReading r = new KanjiReading();
                r.setReading(req.getReading());
                r.setRomaji(req.getRomaji());
                r.setKanjiType(KanjiType.KUN);
                r.setKanji(kanji);
                return r;
            }).toList();
            kanji.getReadings().addAll(kunList);
        }

        if (kanji.getExampleKanjis() != null) {
            kanji.getExampleKanjis().clear();
        } else {
            kanji.setExampleKanjis(new ArrayList<>());
        }

        if (request.getExamples() != null) {
            List<ExampleKanji> newExamples = request.getExamples().stream().map(req -> {
                ExampleKanji e = new ExampleKanji();
                e.setContent(req.getContent());
                e.setMeaning(req.getMeaning());
                e.setDisplayOrder(req.getDisplayOrder());
                e.setKanji(kanji);
                return e;
            }).toList();
            kanji.getExampleKanjis().addAll(newExamples);
        }
    }

    public KanjiAdminResponse toResponse(Kanji kanji) {

        List<KanjiAdminResponse.ReadingDetailResponse> onReadings = kanji.getReadings() == null ? List.of() :
                kanji.getReadings().stream()
                        .filter(r -> r.getKanjiType() == KanjiType.ON)
                        .map(r -> KanjiAdminResponse.ReadingDetailResponse.builder()
                                .id(r.getId()).reading(r.getReading()).romaji(r.getRomaji()).build()
                        ).toList();

        List<KanjiAdminResponse.ReadingDetailResponse> kunReadings = kanji.getReadings() == null ? List.of() :
                kanji.getReadings().stream()
                        .filter(r -> r.getKanjiType() == KanjiType.KUN)
                        .map(r -> KanjiAdminResponse.ReadingDetailResponse.builder()
                                .id(r.getId()).reading(r.getReading()).romaji(r.getRomaji()).build()
                        ).toList();

        List<KanjiAdminResponse.ExampleResponse> exampleResponses = kanji.getExampleKanjis() == null ? List.of() :
                kanji.getExampleKanjis().stream().map(e -> KanjiAdminResponse.ExampleResponse.builder()
                        .id(e.getId()).content(e.getContent()).meaning(e.getMeaning()).displayOrder(e.getDisplayOrder()).build()
                ).toList();

        return KanjiAdminResponse.builder()
                .id(kanji.getId())
                .characters(kanji.getCharacters())
                .meaning(kanji.getMeaning())
                .strokeCount(kanji.getStrokeCount())
                .han(kanji.getHan())
                .levelId(kanji.getLevel().getId())
                .levelName(kanji.getLevel().getLevel().name())
                .onReadings(onReadings)
                .kunReadings(kunReadings)
                .examples(exampleResponses)
                .build();
    }
}