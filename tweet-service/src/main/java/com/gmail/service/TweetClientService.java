package com.gmail.service;

import com.gmail.dto.request.IdsRequest;
import com.gmail.repository.projection.ChatTweetProjection;
import com.gmail.repository.projection.NotificationTweetProjection;
import com.gmail.repository.projection.TweetProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TweetClientService {

    List<TweetProjection> getTweetsByIds(IdsRequest requests);

    Page<TweetProjection> getTweetsByUserIds(IdsRequest request, Pageable pageable);

    TweetProjection getTweetById(Long tweetId);

    NotificationTweetProjection getNotificationTweet(Long tweetId);

    Boolean isTweetExists(Long tweetId);

    Long getTweetCountByText(String text);

    ChatTweetProjection getChatTweet(Long tweetId);
}
