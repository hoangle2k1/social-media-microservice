package com.gmail.service.util;

import com.gmail.repository.TopicRepository;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicProjectionHelper {

    private final TopicRepository topicRepository;

    public boolean isTopicFollowed(Long topicId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return topicRepository.isTopicFollowed(authUserId, topicId);
    }

    public boolean isTopicNotInterested(Long topicId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return topicRepository.isTopicNotInterested(authUserId, topicId);
    }
}
