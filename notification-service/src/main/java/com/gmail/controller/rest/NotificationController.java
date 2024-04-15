package com.gmail.controller.rest;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.NotificationInfoResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.dto.response.notification.NotificationUserResponse;
import com.gmail.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gmail.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UI_V1_NOTIFICATION)
public class NotificationController {

    private final NotificationMapper notificationMapper;

    @GetMapping(USER)
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<NotificationResponse> response = notificationMapper.getUserNotifications(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(MENTIONS)
    public ResponseEntity<List<TweetResponse>> getUserMentionsNotifications(@PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<TweetResponse> response = notificationMapper.getUserMentionsNotifications(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(SUBSCRIBES)
    public ResponseEntity<List<NotificationUserResponse>> getTweetAuthorsNotifications() {
        return ResponseEntity.ok(notificationMapper.getTweetAuthorsNotifications());
    }

    @GetMapping(NOTIFICATION_ID)
    public ResponseEntity<NotificationInfoResponse> getUserNotificationById(@PathVariable("notificationId") Long notificationId) {
        return ResponseEntity.ok(notificationMapper.getUserNotificationById(notificationId));
    }

    @GetMapping(TIMELINE)
    public ResponseEntity<List<TweetResponse>> getNotificationsFromTweetAuthors(@PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<TweetResponse> response = notificationMapper.getNotificationsFromTweetAuthors(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }
}
