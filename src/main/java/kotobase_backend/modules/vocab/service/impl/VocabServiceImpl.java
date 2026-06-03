package kotobase_backend.modules.vocab.service.impl;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.progress.repository.UserItemProgressRepository;
import kotobase_backend.modules.topic.repository.TopicRepository;
import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.PageVocabResponse;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.mapper.VocabMapper;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import kotobase_backend.modules.vocab.service.VocabService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabServiceImpl implements VocabService {

    private final VocabRepository vocabRepository;
    private final VocabMapper vocabMapper;
    private final TopicRepository topicRepository;
    private final UserItemProgressRepository userItemProgressRepository;

    @Override
    public PageVocabResponse<VocabResponse> getAllVocabs(VocabRequest request, Integer userId) {

        boolean check = topicRepository.existsById(request.getTopicId());
        if(!check){
            throw new ResourceNotFoundException("không tìm thâý topic");
        }

            int limit = request.getLimit();
            int page = request.getPage() - 1 ;

            Pageable pageable = PageRequest.of(page, limit);

            Page<Vocab> pageVocab = vocabRepository.findByTopicId(request.getTopicId(), pageable);

        final List<Integer> savedVocabIds;
        if (userId!= null) {
            savedVocabIds = userItemProgressRepository.findSavedItemIds(userId, ItemType.VOCAB);
        }
        else {
            savedVocabIds = new ArrayList<>();
        }

        List<VocabResponse> data = pageVocab.getContent().stream()
                .map(vocab -> {
                    boolean isSaved = savedVocabIds.contains(vocab.getId());
                    return vocabMapper.mapToVocab(vocab, isSaved);
                })
                .toList();

        PageVocabResponse<VocabResponse> res = new PageVocabResponse<>();
        res.setData(data);
        res.setPage(page);
        res.setLimit(limit);
        res.setTotalPages(pageVocab.getTotalPages());
        res.setTotalElements(pageVocab.getTotalElements());
        return res;
    }

//    @Override
//    public VocabResponse getVocabById(Integer id) {
//        Vocab newvocab = vocabRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay tu vung"));
//        return vocabMapper.mapToVocab(newvocab);
//    }
}
