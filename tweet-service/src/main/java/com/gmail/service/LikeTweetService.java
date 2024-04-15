package com.gmail.service;

import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.repository.projection.LikeTweetProjection;
import com.gmail.repository.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeTweetService {

    Page<LikeTweetProjection> getUserLikedTweets(Long userId, Pageable pageable);

    Page<UserProjection> getLikedUsersByTweetId(Long tweetId, Pageable pageable);

    NotificationResponse likeTweet(Long tweetId);
}
