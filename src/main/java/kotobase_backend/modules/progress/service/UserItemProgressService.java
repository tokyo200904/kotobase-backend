package kotobase_backend.modules.progress.service;

import kotobase_backend.modules.progress.dto.request.ItemRequest;

public interface UserItemProgressService {
    public boolean addOrUnAdd(Integer userId, ItemRequest itemRequest);
}
