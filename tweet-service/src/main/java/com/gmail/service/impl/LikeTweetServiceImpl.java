package com.gmail.service.impl;

import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.enums.NotificationType;
import com.gmail.model.LikeTweet;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.producer.UpdateTweetCountProducer;
import com.gmail.repository.LikeTweetRepository;
import com.gmail.repository.projection.LikeTweetProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.LikeTweetService;
import com.gmail.service.UserService;
import com.gmail.service.util.TweetServiceHelper;
import com.gmail.service.util.TweetValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeTweetServiceImpl implements LikeTweetService {

    private final LikeTweetRepository likeTweetRepository;
    private final TweetServiceHelper tweetServiceHelper;
    private final TweetValidationHelper tweetValidationHelper;
    private final UpdateTweetCountProducer updateTweetCountProducer;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<LikeTweetProjection> getUserLikedTweets(Long userId, Pageable pageable) {
        tweetValidationHelper.validateUserProfile(userId);
        return likeTweetRepository.getUserLikedTweets(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProjection> getLikedUsersByTweetId(Long tweetId, Pageable pageable) {
        Tweet tweet = tweetValidationHelper.checkValidTweet(tweetId);
        return userService.getLikedUsersByTweet(tweet, pageable);
    }

    @Override
    @Transactional
    public NotificationResponse likeTweet(Long tweetId) {
        Tweet tweet = tweetValidationHelper.checkValidTweet(tweetId);
        User authUser = userService.getAuthUser();
        LikeTweet likedTweet = likeTweetRepository.getLikedTweet(authUser, tweet);
        boolean isTweetLiked;

        if (likedTweet != null) {
            likeTweetRepository.delete(likedTweet);
            isTweetLiked = false;
        } else {
            LikeTweet newLikeTweet = new LikeTweet(authUser, tweet);
            likeTweetRepository.save(newLikeTweet);
            isTweetLiked = true;
        }
        updateTweetCountProducer.sendUpdateLikeTweetCountEvent(authUser.getId(), isTweetLiked);
        return tweetServiceHelper.sendNotification(NotificationType.LIKE, isTweetLiked, tweet.getAuthor().getId(), authUser.getId(), tweetId);
    }
}
