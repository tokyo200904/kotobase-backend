package kotobase_backend.modules.progress;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.dto.request.StationAdminRequest;
import kotobase_backend.modules.progress.dto.response.StationAdminResponse;
import kotobase_backend.modules.progress.service.StationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/station")
@RequiredArgsConstructor
public class StationAdminController {
    private final StationAdminService stationAdminService;

    @GetMapping
    public ResponseEntity<PageResponse<StationAdminResponse>> getAllStations(@RequestParam(required = false) String search,
                                                                             @RequestParam(required = false) Integer levelId,
                                                                             @RequestParam(required = false) ItemType itemType,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(stationAdminService.getAllStations(search, levelId, itemType, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationAdminResponse> getStationById(@PathVariable Integer id) {
        return ResponseEntity.ok(stationAdminService.getStationById(id));
    }

    @PostMapping
    public ResponseEntity<StationAdminResponse> createStation(@RequestBody StationAdminRequest request) {
        return ResponseEntity.ok(stationAdminService.createStation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationAdminResponse> updateStation(@PathVariable Integer id, @RequestBody StationAdminRequest request) {
        return ResponseEntity.ok(stationAdminService.updateStation(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable Integer id) {
        stationAdminService.deleteStation(id);
        return ResponseEntity.ok("Xóa trạm học thành công!");
    }
}