package kotobase_backend.modules.JlptLevel;

import kotobase_backend.modules.JlptLevel.dto.response.LevelCompactResponse;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/level")
@RequiredArgsConstructor
public class JlptLevelAdminController {
    private final JlptLevelRepository levelRepository;

    @GetMapping("/compact")
    public ResponseEntity<List<LevelCompactResponse>> getCompactLevels() {
        List<LevelCompactResponse> response = levelRepository.findAll().stream()
                .map(l -> LevelCompactResponse.builder()
                        .id(l.getId())
                        .levelName(l.getLevel().name())
                        .build())
                .toList();
        return ResponseEntity.ok(response);
    }
}