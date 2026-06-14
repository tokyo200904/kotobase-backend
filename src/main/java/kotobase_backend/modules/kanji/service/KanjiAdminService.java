package kotobase_backend.modules.kanji.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.kanji.dto.Request.KanjiAdminRequest;
import kotobase_backend.modules.kanji.dto.Response.KanjiAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KanjiAdminService {
    PageResponse<KanjiAdminResponse> getAllKanjis(String search, Integer levelId, Pageable pageable);
    KanjiAdminResponse getKanjiById(Integer id);
    KanjiAdminResponse createKanji(KanjiAdminRequest request);
    KanjiAdminResponse updateKanji(Integer id, KanjiAdminRequest request);
    void deleteKanji(Integer id);
}