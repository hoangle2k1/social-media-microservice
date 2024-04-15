package com.gmail.service.util;

import com.gmail.dto.response.notification.NotificationListResponse;
import com.gmail.dto.response.notification.NotificationTweetResponse;
import com.gmail.dto.response.notification.NotificationUserResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.dto.response.user.UserResponse;
import com.gmail.feign.ListsClient;
import com.gmail.feign.TweetClient;
import com.gmail.feign.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationServiceHelper {

    private final UserClient userClient;
    private final TweetClient tweetClient;
    private final ListsClient listsClient;

    public UserResponse getUserById(Long userId) {
        return userClient.getUserById(userId);
    }

    public TweetResponse getTweetById(Long tweetId) {
        return tweetClient.getTweetById(tweetId);
    }

    public NotificationUserResponse getNotificationUser(Long userId) {
        return userClient.getNotificationUser(userId);
    }

    public NotificationTweetResponse getNotificationTweet(Long userId) {
        return tweetClient.getNotificationTweet(userId);
    }

    public NotificationListResponse getNotificationList(Long listId) {
        return listsClient.getNotificationList(listId);
    }
}
