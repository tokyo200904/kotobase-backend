package kotobase_backend.modules.JlptLevel.mapper;

import kotobase_backend.modules.JlptLevel.dto.response.JlptLevelResponse;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import org.springframework.stereotype.Component;

@Component
public class JlptLevelMapper {
    public JlptLevelResponse toJlptLevelResponse(JlptLevel jlptLevel) {
        JlptLevelResponse jlptLevelResponse = new JlptLevelResponse();
        jlptLevelResponse.setId(jlptLevel.getId());
        jlptLevelResponse.setLevel(jlptLevel.getLevel());
        return jlptLevelResponse;
    }
}
