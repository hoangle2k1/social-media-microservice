package com.gmail.service.impl;

import com.gmail.dto.response.tweet.TweetListResponse;
import com.gmail.dto.response.notification.NotificationListResponse;
import com.gmail.mapper.BasicMapper;
import com.gmail.repository.ListsRepository;
import com.gmail.repository.projection.NotificationListProjection;
import com.gmail.repository.projection.TweetListProjection;
import com.gmail.service.ListsClientService;
import com.gmail.service.UserService;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListsClientServiceImpl implements ListsClientService {

    private final ListsRepository listsRepository;
    private final UserService userService;
    private final BasicMapper basicMapper;

    @Override
    public NotificationListResponse getNotificationList(Long listId) {
        NotificationListProjection list = listsRepository.getListById(listId, NotificationListProjection.class);
        return basicMapper.convertToResponse(list, NotificationListResponse.class);
    }

    @Override
    public TweetListResponse getTweetList(Long listId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        Optional<TweetListProjection> list = listsRepository.getListById(listId, authUserId, TweetListProjection.class);

        if (list.isEmpty() || userService.isUserBlocked(list.get().getListOwner().getId(), authUserId)) {
            return new TweetListResponse();
        }
        if (!authUserId.equals(list.get().getListOwner().getId()) && userService.isUserHavePrivateProfile(list.get().getListOwner().getId(), authUserId)) {
            return new TweetListResponse();
        }
        return basicMapper.convertToResponse(list.get(), TweetListResponse.class);
    }
}
