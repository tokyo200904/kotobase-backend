package kotobase_backend.modules.progress.service.impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.progress.dto.request.StationAdminRequest;
import kotobase_backend.modules.progress.dto.response.StationAdminResponse;
import kotobase_backend.modules.progress.entity.Station;
import kotobase_backend.modules.progress.mapper.StationAdminMapper;
import kotobase_backend.modules.progress.repository.StationRepository;
import kotobase_backend.modules.progress.service.StationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StationAdminServiceImpl implements StationAdminService {

    private final StationRepository stationRepository;
    private final StationAdminMapper stationMapper;

    @Override
    public PageResponse<StationAdminResponse> getAllStations(String search, Integer levelId, ItemType itemType, Pageable pageable) {
        Page<Station> page = stationRepository.adminSearchStations(search, levelId, itemType, pageable);
        return PageResponse.of(page.map(stationMapper::toResponse));
    }

    @Override
    public StationAdminResponse getStationById(Integer id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trạm học"));
        return stationMapper.toResponse(station);
    }

    @Override
    @Transactional
    public StationAdminResponse createStation(StationAdminRequest request) {
        Station station = new Station();
        stationMapper.updateEntityFromRequest(request, station);
        Station saved = stationRepository.save(station);
        return stationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public StationAdminResponse updateStation(Integer id, StationAdminRequest request) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trạm học"));
        stationMapper.updateEntityFromRequest(request, station);
        Station updated = stationRepository.save(station);
        return stationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteStation(Integer id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trạm học"));
        stationRepository.delete(station);
    }
}