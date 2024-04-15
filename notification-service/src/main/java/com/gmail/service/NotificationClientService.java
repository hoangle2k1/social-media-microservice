package com.gmail.service;

import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.model.Notification;

public interface NotificationClientService {

    NotificationResponse sendNotification(Notification notification, boolean notificationCondition);

    void sendTweetMentionNotification(Notification notification);

    void sendTweetNotificationToSubscribers(Long tweetId);
}
