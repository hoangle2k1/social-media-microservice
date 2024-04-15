package com.gmail.service;

import com.gmail.dto.response.tweet.TweetListResponse;
import com.gmail.dto.response.notification.NotificationListResponse;

public interface ListsClientService {

    NotificationListResponse getNotificationList(Long listId);

    TweetListResponse getTweetList(Long listId);
}
