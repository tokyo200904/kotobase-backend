package kotobase_backend.modules.JlptLevel.service.Impl;

import kotobase_backend.modules.JlptLevel.dto.response.JlptLevelResponse;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.mapper.JlptLevelMapper;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.JlptLevel.service.JlptLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JlptLevelServiceImpl implements JlptLevelService {

    private final JlptLevelRepository jlptLevelRepository;

    private final JlptLevelMapper jlptLevelMapper;

    @Override
    public List<JlptLevelResponse> getJlptLevels() {
        List<JlptLevel> jlptLevels = jlptLevelRepository.findAll();
        return jlptLevels.stream()
                .map(jlptLevelMapper::toJlptLevelResponse)
                .toList();
    }
}
