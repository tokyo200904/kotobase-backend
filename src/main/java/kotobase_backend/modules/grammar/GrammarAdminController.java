package kotobase_backend.modules.grammar;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.grammar.dto.request.GrammarAdminRequest;
import kotobase_backend.modules.grammar.dto.response.GrammarAdminResponse;
import kotobase_backend.modules.grammar.service.GrammarAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1admin/grammar")
@RequiredArgsConstructor
public class GrammarAdminController {
    private final GrammarAdminService grammarAdminService;

    @GetMapping
    public ResponseEntity<PageResponse<GrammarAdminResponse>> getAllGrammars(@RequestParam(required = false) String search,
                                                                             @RequestParam(required = false) Integer levelId,
                                                                             @RequestParam(required = false) Integer lessonId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(grammarAdminService.getAllGrammars(search, levelId, lessonId, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrammarAdminResponse> getGrammarById(@PathVariable Integer id) {
        return ResponseEntity.ok(grammarAdminService.getGrammarById(id));
    }

    @PostMapping
    public ResponseEntity<GrammarAdminResponse> createGrammar(@RequestBody GrammarAdminRequest request) {
        return ResponseEntity.ok(grammarAdminService.createGrammar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrammarAdminResponse> updateGrammar(@PathVariable Integer id,
                                                              @RequestBody GrammarAdminRequest request) {
        return ResponseEntity.ok(grammarAdminService.updateGrammar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGrammar(@PathVariable Integer id) {
        grammarAdminService.deleteGrammar(id);
        return ResponseEntity.ok("Xóa ngữ pháp thành công!");
    }
}