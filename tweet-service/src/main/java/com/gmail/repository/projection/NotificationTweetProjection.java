package com.gmail.repository.projection;

import org.springframework.beans.factory.annotation.Value;

public interface NotificationTweetProjection {
    Long getId();
    String getText();

    @Value("#{target.author.id}")
    Long getAuthorId();
}
