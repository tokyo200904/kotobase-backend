package kotobase_backend.modules.examples.mapper;

import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import kotobase_backend.modules.examples.entity.ExampleGrammar;
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

    public ExampleResponse toExampleResponseGrammar(ExampleGrammar exampleGrammar) {
        ExampleResponse er = new ExampleResponse();
        er.setId(exampleGrammar.getId());
        er.setMeaning(exampleGrammar.getMeaning());
        er.setContent(exampleGrammar.getContent());
        return er;
    }
}
