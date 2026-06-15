package kotobase_backend.modules.progress.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.dto.request.BulkReorderRequest;
import kotobase_backend.modules.progress.dto.request.StationAdminRequest;
import kotobase_backend.modules.progress.dto.response.StationAdminResponse;
import org.springframework.data.domain.Pageable;

public interface StationAdminService {
    PageResponse<StationAdminResponse> getAllStations(String search, Integer levelId, ItemType itemType, Pageable pageable);
    StationAdminResponse getStationById(Integer id);
    StationAdminResponse createStation(StationAdminRequest request);
    StationAdminResponse updateStation(Integer id, StationAdminRequest request);
    void deleteStation(Integer id);
    void updateBulkOrder(BulkReorderRequest request);
}
