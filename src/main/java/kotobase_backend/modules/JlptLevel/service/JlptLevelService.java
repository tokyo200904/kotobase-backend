package kotobase_backend.modules.JlptLevel.service;

import kotobase_backend.modules.JlptLevel.dto.response.JlptLevelResponse;

import java.util.List;

public interface JlptLevelService {
    public List<JlptLevelResponse> getJlptLevels();
}
