package com.gmail.service;

import com.gmail.enums.TopicCategory;
import com.gmail.dto.response.TopicsByCategoriesResponse;
import com.gmail.repository.projection.FollowedTopicProjection;
import com.gmail.repository.projection.NotInterestedTopicProjection;
import com.gmail.repository.projection.TopicProjection;

import java.util.List;

public interface TopicService {

    List<TopicProjection> getTopicsByIds(List<Long> topicsIds);

    List<TopicsByCategoriesResponse> getTopicsByCategories(List<TopicCategory> categories);

    List<FollowedTopicProjection> getFollowedTopics();

    List<TopicProjection> getFollowedTopicsByUserId(Long userId);

    List<NotInterestedTopicProjection> getNotInterestedTopics();

    Boolean processNotInterestedTopic(Long topicId);

    Boolean processFollowTopic(Long topicId);
}
