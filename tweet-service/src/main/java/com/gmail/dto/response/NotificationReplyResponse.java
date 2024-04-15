package com.gmail.dto.response;

import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationReplyResponse {
    private Long tweetId;
    private NotificationType notificationType;
    private TweetResponse tweet;
}
