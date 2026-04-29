package kotobase_backend.modules.example.mapper;

import kotobase_backend.modules.example.dto.response.ExampleResponse;
import kotobase_backend.modules.example.entity.Example;
import org.springframework.stereotype.Component;

@Component
public class ExampleMapper {
    public ExampleResponse toExampleResponse(Example example) {
        ExampleResponse er = new ExampleResponse();
        er.setId(example.getId());
        er.setMeaning(example.getMeaning());
        er.setContent(example.getContent());
        er.setRomaji(example.getRomaji());
        return er;
    }
}
