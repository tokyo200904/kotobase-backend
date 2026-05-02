package kotobase_backend.modules.grammar.mapper;

import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import kotobase_backend.modules.examples.mapper.ExampleMapper;
import kotobase_backend.modules.grammar.dto.response.GrammarResponse;
import kotobase_backend.modules.grammar.dto.response.GrammarTitleResponse;
import kotobase_backend.modules.grammar.entity.Grammar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GrammarMapper {

    private final ExampleMapper exampleMapper;

    public GrammarResponse toGrammarResponse(Grammar grammar) {
        List<ExampleResponse> example =
                grammar.getExampleGrammars() == null ? List.of() : grammar.getExampleGrammars().stream()
                        .map(exampleMapper::toExampleResponseGrammar)
                        .toList();

        GrammarResponse gr = new GrammarResponse();
        gr.setId(grammar.getId());
        gr.setTitle(grammar.getTitle());
        gr.setNote(grammar.getNote());
        gr.setMeaning(grammar.getMeaning());
        gr.setUsages(grammar.getUsages());
        gr.setStructure(grammar.getStructure());
        gr.setExamples(example);
        return gr;
    }

    public GrammarTitleResponse toGrammarTitleResponse(Grammar grammar) {
        GrammarTitleResponse gr = new GrammarTitleResponse();
        gr.setId(grammar.getId());
        gr.setTitle(grammar.getTitle());
        return gr;
    }
}
