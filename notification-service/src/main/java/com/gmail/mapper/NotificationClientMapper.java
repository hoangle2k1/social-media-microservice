package com.gmail.mapper;

import com.gmail.dto.request.NotificationRequest;
import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.model.Notification;
import com.gmail.service.NotificationClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationClientMapper {

    private final BasicMapper basicMapper;
    private final NotificationClientService notificationClientService;

    public NotificationResponse sendNotification(NotificationRequest request) {
        Notification notification = basicMapper.convertToResponse(request, Notification.class);
        return notificationClientService.sendNotification(notification, request.isNotificationCondition());
    }

    public void sendTweetMentionNotification(NotificationRequest request) {
        Notification notification = basicMapper.convertToResponse(request, Notification.class);
        notificationClientService.sendTweetMentionNotification(notification);
    }

    public void sendTweetNotificationToSubscribers(Long tweetId) {
        notificationClientService.sendTweetNotificationToSubscribers(tweetId);
    }
}
