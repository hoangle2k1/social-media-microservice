package com.gmail.repository.projection;

import com.gmail.enums.TopicCategory;
import org.springframework.beans.factory.annotation.Value;

public interface NotInterestedTopicProjection {
    Long getId();
    String getTopicName();
    TopicCategory getTopicCategory();

    @Value("#{true}")
    boolean getIsTopicNotInterested();
}
