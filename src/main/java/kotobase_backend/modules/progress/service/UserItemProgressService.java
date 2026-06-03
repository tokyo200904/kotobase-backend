package kotobase_backend.modules.progress.service;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.dto.request.ItemRequest;
import kotobase_backend.modules.progress.dto.request.SubmitReviewRequest;
import kotobase_backend.modules.progress.dto.response.QuizQuestionResponse;

import java.util.List;

public interface UserItemProgressService {
    public boolean addOrUnAdd(Integer userId, ItemRequest itemRequest);
    public QuizQuestionResponse getNextQuestion(Integer userId, ItemType type);
    public void submitAnswer(Integer userId, SubmitReviewRequest request);
    public List<QuizQuestionResponse> getExtraPracticeQuestion(Integer userId, ItemType type);
    public void submitExtraPracticeAnswer(Integer userId, SubmitReviewRequest request);

}
