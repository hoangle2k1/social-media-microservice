package com.gmail.mapper;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.dto.response.user.UserResponse;
import com.gmail.repository.projection.LikeTweetProjection;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.LikeTweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeTweetMapper {

    private final BasicMapper basicMapper;
    private final LikeTweetService likeTweetService;

    public HeaderResponse<TweetResponse> getUserLikedTweets(Long userId, Pageable pageable) {
        Page<LikeTweetProjection> userLikedTweets = likeTweetService.getUserLikedTweets(userId, pageable);
        List<TweetProjection> tweets = new ArrayList<>();
        userLikedTweets.getContent().forEach(likeTweet -> tweets.add(likeTweet.getTweet()));
        return basicMapper.getHeaderResponse(tweets, userLikedTweets.getTotalPages(), TweetResponse.class);
    }

    public HeaderResponse<UserResponse> getLikedUsersByTweetId(Long tweetId, Pageable pageable) {
        Page<UserProjection> users = likeTweetService.getLikedUsersByTweetId(tweetId, pageable);
        return basicMapper.getHeaderResponse(users, UserResponse.class);
    }

    public NotificationResponse likeTweet(Long tweetId) {
        return likeTweetService.likeTweet(tweetId);
    }
}
