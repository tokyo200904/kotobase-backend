package kotobase_backend.modules.progress.service;

import kotobase_backend.modules.progress.dto.response.DashboardStatsResponse;

public interface StatisticsService {
    public DashboardStatsResponse getDashboardStats(Integer userId);
}
