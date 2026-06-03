package kotobase_backend.modules.kanji;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjiFindResponse;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;
import kotobase_backend.modules.kanji.service.KanjiService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/kanji")
@RequiredArgsConstructor
public class KanjiController {
    private final KanjiService kanjiService;

    @GetMapping
    public ResponseEntity<List<KanjisResponse>> getKanjiLvel(@RequestParam Level level) {
        return ResponseEntity.ok(kanjiService.getKanji(level));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KanjiDetelResponse> getDetelKanji(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Integer id) {
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(kanjiService.getKanjiDetel(id, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<KanjiFindResponse>> findKanjis(@RequestParam String keyword) {
        return ResponseEntity.ok(kanjiService.findKanji(keyword));
    }

}
