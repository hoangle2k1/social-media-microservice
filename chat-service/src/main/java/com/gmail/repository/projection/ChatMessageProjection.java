package com.gmail.repository.projection;

import com.gmail.dto.response.chat.ChatTweetResponse;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface ChatMessageProjection {
    Long getId();
    String getText();
    LocalDateTime getDate();
    Long getTweetId();
    UserProjection getAuthor();
    ChatProjection getChat();

    @Value("#{target.tweetId == null ? null : @chatServiceHelper.getChatTweet(target.tweetId)}")
    ChatTweetResponse getTweet();

    interface UserProjection {
        Long getId();
    }

    interface ChatProjection {
        Long getId();
    }
}
