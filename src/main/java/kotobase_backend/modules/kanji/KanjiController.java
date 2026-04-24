package kotobase_backend.modules.kanji;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;
import kotobase_backend.modules.kanji.service.KanjiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/kanji")
@RequiredArgsConstructor
public class KanjiController {
    private final KanjiService kanjiService;

    @GetMapping
    public ResponseEntity<List<KanjisResponse>> getKanjiLvel(@RequestParam Level level) {
        return ResponseEntity.ok(kanjiService.getKanji(level));
    }

}
