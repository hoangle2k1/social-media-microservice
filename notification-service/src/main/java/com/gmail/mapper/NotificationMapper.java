package com.gmail.mapper;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.NotificationInfoResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.dto.response.notification.NotificationUserResponse;
import com.gmail.repository.projection.NotificationInfoProjection;
import com.gmail.repository.projection.NotificationProjection;
import com.gmail.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final BasicMapper basicMapper;
    private final NotificationService notificationService;

    public HeaderResponse<NotificationResponse> getUserNotifications(Pageable pageable) {
        Page<NotificationProjection> notifications = notificationService.getUserNotifications(pageable);
        return basicMapper.getHeaderResponse(notifications, NotificationResponse.class);
    }

    public HeaderResponse<TweetResponse> getUserMentionsNotifications(Pageable pageable) {
        Page<TweetResponse> tweets = notificationService.getUserMentionsNotifications(pageable);
        return basicMapper.getHeaderResponse(tweets, TweetResponse.class);
    }

    public List<NotificationUserResponse> getTweetAuthorsNotifications() {
        return notificationService.getTweetAuthorsNotifications();
    }

    public NotificationInfoResponse getUserNotificationById(Long notificationId) {
        NotificationInfoProjection notification = notificationService.getUserNotificationById(notificationId);
        return basicMapper.convertToResponse(notification, NotificationInfoResponse.class);
    }

    public HeaderResponse<TweetResponse> getNotificationsFromTweetAuthors(Pageable pageable) {
        Page<TweetResponse> tweets = notificationService.getNotificationsFromTweetAuthors(pageable);
        return basicMapper.getHeaderResponse(tweets, TweetResponse.class);
    }
}
