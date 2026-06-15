package kotobase_backend.modules.progress.mapper;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.progress.dto.request.StationAdminRequest;
import kotobase_backend.modules.progress.dto.response.StationAdminResponse;
import kotobase_backend.modules.progress.entity.Station;
import kotobase_backend.modules.progress.entity.StationItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StationAdminMapper {

    private final JlptLevelRepository levelRepository;

    public void updateEntityFromRequest(StationAdminRequest request, Station station) {
        station.setItemType(request.getItemType());
        station.setStationOrder(request.getStationOrder());
        station.setTitle(request.getTitle());
        station.setDescription(request.getDescription());

        JlptLevel level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Level"));
        station.setLevel(level);
        if (station.getStationItems() != null) {
            station.getStationItems().clear();
        } else {
            station.setStationItems(new ArrayList<>());
        }

        if (request.getTargetItemIds() != null) {
            List<StationItem> newItems = request.getTargetItemIds().stream().map(itemId -> {
                StationItem item = new StationItem();
                item.setTargetItemId(itemId);
                item.setStation(station);
                return item;
            }).toList();

            station.getStationItems().addAll(newItems);
        }
    }

    public StationAdminResponse toResponse(Station station) {
        List<Integer> itemIds = station.getStationItems() == null ? List.of() :
                station.getStationItems().stream()
                        .map(StationItem::getTargetItemId)
                        .toList();

        return StationAdminResponse.builder()
                .id(station.getId())
                .levelId(station.getLevel().getId())
                .levelName(station.getLevel().getLevel().name())
                .itemType(station.getItemType())
                .stationOrder(station.getStationOrder())
                .title(station.getTitle())
                .description(station.getDescription())
                .totalItems(itemIds.size())
                .targetItemIds(itemIds)
                .build();
    }
}