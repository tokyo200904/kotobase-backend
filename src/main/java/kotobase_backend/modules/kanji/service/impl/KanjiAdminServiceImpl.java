package kotobase_backend.modules.kanji.service.impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.kanji.dto.Request.KanjiAdminRequest;
import kotobase_backend.modules.kanji.dto.Response.KanjiAdminResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiCompactResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.kanji.mapper.KanjiAdminMapper;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.kanji.service.KanjiAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KanjiAdminServiceImpl implements KanjiAdminService {

    private final KanjiRepository kanjiRepository;
    private final KanjiAdminMapper kanjiMapper;

    @Override
    public PageResponse<KanjiAdminResponse> getAllKanjis(String search, Integer levelId, Pageable pageable) {
        Page<Kanji> kanjiPage = kanjiRepository.adminSearchKanjis(search, levelId, pageable);
        Page<KanjiAdminResponse> responsePage = kanjiPage.map(kanjiMapper::toResponse);
        return PageResponse.of(responsePage);
    }

    @Override
    public KanjiAdminResponse getKanjiById(Integer id) {
        Kanji kanji = kanjiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Kanji"));
        return kanjiMapper.toResponse(kanji);
    }

    @Override
    @Transactional
    public KanjiAdminResponse createKanji(KanjiAdminRequest request) {
        Kanji kanji = new Kanji();

        kanjiMapper.updateEntityFromRequest(request, kanji);
        Kanji savedKanji = kanjiRepository.save(kanji);
        return kanjiMapper.toResponse(savedKanji);
    }

    @Override
    public KanjiAdminResponse updateKanji(Integer id, KanjiAdminRequest request) {

        Kanji kanji = kanjiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Kanji"));
        kanjiMapper.updateEntityFromRequest(request, kanji);

        Kanji updatedKanji = kanjiRepository.save(kanji);
        return kanjiMapper.toResponse(updatedKanji);
    }

    @Override
    public List<KanjiCompactResponse> getCompactKanjisByLevel(Integer levelId) {
        return kanjiRepository.findByLevel_IdOrderByIdAsc(levelId).stream()
                .map(k -> KanjiCompactResponse.builder()
                        .id(k.getId())
                        .characters(k.getCharacters())
                        .meaning(k.getMeaning())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void deleteKanji(Integer id) {
        Kanji kanji = kanjiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Kanji"));
        kanjiRepository.delete(kanji);
    }
}
