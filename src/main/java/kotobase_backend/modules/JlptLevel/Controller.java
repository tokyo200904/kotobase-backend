package kotobase_backend.modules.JlptLevel;

import kotobase_backend.modules.JlptLevel.dto.response.JlptLevelResponse;
import kotobase_backend.modules.JlptLevel.service.JlptLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/level")
@RequiredArgsConstructor
public class Controller {
    private final JlptLevelService jlptLevelService;

    @GetMapping
    public ResponseEntity<List<JlptLevelResponse>> getJlptLevels() {
        return ResponseEntity.ok(jlptLevelService.getJlptLevels());
    }
}
