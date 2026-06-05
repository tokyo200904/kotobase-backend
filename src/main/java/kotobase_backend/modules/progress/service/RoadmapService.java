package kotobase_backend.modules.progress.service;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.dto.response.StationItemsResponse;
import kotobase_backend.modules.progress.dto.response.StationResponse;
import kotobase_backend.modules.progress.dto.response.SubmitTestRequest;

import java.util.List;
import java.util.Map;

public interface RoadmapService {
    public List<StationResponse> getRoadmap(Integer userId, Integer levelId, ItemType itemType);
    public void completeStation(Integer userId, Integer stationId);
    public StationItemsResponse getStationItems(Integer stationId);
    public Map<String, Object> submitAndEvaluateTest(Integer userId, Integer stationId, SubmitTestRequest request);
}
