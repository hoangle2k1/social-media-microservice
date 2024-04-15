package com.gmail.repository.projection;

import com.gmail.enums.TopicCategory;
import org.springframework.beans.factory.annotation.Value;

public interface TopicProjection {
    Long getId();
    String getTopicName();
    TopicCategory getTopicCategory();

    @Value("#{@topicProjectionHelper.isTopicFollowed(target.id)}")
    boolean getIsTopicFollowed();

    @Value("#{@topicProjectionHelper.isTopicNotInterested(target.id)}")
    boolean getIsTopicNotInterested();
}
