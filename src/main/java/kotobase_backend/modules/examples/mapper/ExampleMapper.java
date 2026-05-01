package kotobase_backend.modules.examples.mapper;

import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import kotobase_backend.modules.examples.entity.ExampleVocab;
import org.springframework.stereotype.Component;

@Component
public class ExampleMapper {
    public ExampleResponse toExampleResponse(ExampleVocab exampleVocab) {
        ExampleResponse er = new ExampleResponse();
        er.setId(exampleVocab.getId());
        er.setMeaning(exampleVocab.getMeaning());
        er.setContent(exampleVocab.getContent());
        return er;
    }
}
