package kotobase_backend.modules.kanji;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.kanji.dto.Request.KanjiAdminRequest;
import kotobase_backend.modules.kanji.dto.Response.KanjiAdminResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiCompactResponse;
import kotobase_backend.modules.kanji.service.KanjiAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/kanji")
@RequiredArgsConstructor
public class KanjiAdminController {
    private final KanjiAdminService kanjiAdminService;

    @GetMapping
    public ResponseEntity<PageResponse<KanjiAdminResponse>> getAllKanjis(@RequestParam(required = false) String search,
                                                                         @RequestParam(required = false) Integer levelId,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(kanjiAdminService.getAllKanjis(search, levelId, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKanjiById(@PathVariable Integer id) {
        return ResponseEntity.ok(kanjiAdminService.getKanjiById(id));
    }

    @PostMapping
    public ResponseEntity<?> createKanji(@RequestBody KanjiAdminRequest request) {
        return ResponseEntity.ok(kanjiAdminService.createKanji(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKanji(@PathVariable Integer id, @RequestBody KanjiAdminRequest request) {
        return ResponseEntity.ok(kanjiAdminService.updateKanji(id, request));
    }

    @GetMapping("/compact")
    public ResponseEntity<List<KanjiCompactResponse>> getCompactKanjis(@RequestParam Integer levelId) {
        return ResponseEntity.ok(kanjiAdminService.getCompactKanjisByLevel(levelId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKanji(@PathVariable Integer id) {
        kanjiAdminService.deleteKanji(id);
        return ResponseEntity.ok("Xóa Kanji thành công!");
    }
}