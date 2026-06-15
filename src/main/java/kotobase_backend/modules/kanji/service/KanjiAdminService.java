package kotobase_backend.modules.kanji.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.kanji.dto.Request.KanjiAdminRequest;
import kotobase_backend.modules.kanji.dto.Response.KanjiAdminResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiCompactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KanjiAdminService {
    PageResponse<KanjiAdminResponse> getAllKanjis(String search, Integer levelId, Pageable pageable);
    KanjiAdminResponse getKanjiById(Integer id);
    KanjiAdminResponse createKanji(KanjiAdminRequest request);
    KanjiAdminResponse updateKanji(Integer id, KanjiAdminRequest request);
    List<KanjiCompactResponse> getCompactKanjisByLevel(Integer levelId);
    void deleteKanji(Integer id);
}