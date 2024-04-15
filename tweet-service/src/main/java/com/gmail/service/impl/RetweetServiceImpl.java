package com.gmail.service.impl;

import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.enums.NotificationType;
import com.gmail.model.Retweet;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.producer.UpdateTweetCountProducer;
import com.gmail.repository.RetweetRepository;
import com.gmail.repository.TweetRepository;
import com.gmail.repository.projection.RetweetProjection;
import com.gmail.repository.projection.TweetUserProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.RetweetService;
import com.gmail.service.UserService;
import com.gmail.service.util.TweetServiceHelper;
import com.gmail.service.util.TweetValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final TweetRepository tweetRepository;
    private final RetweetRepository retweetRepository;
    private final TweetServiceHelper tweetServiceHelper;
    private final TweetValidationHelper tweetValidationHelper;
    private final UpdateTweetCountProducer updateTweetCountProducer;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<TweetUserProjection> getUserRetweetsAndReplies(Long userId, Pageable pageable) {
        tweetValidationHelper.validateUserProfile(userId);
        List<TweetUserProjection> replies = tweetRepository.getRepliesByUserId(userId);
        List<RetweetProjection> retweets = retweetRepository.getRetweetsByUserId(userId);
        List<TweetUserProjection> userTweets = tweetServiceHelper.combineTweetsArrays(replies, retweets);
        return tweetServiceHelper.getPageableTweetProjectionList(pageable, userTweets, replies.size() + retweets.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProjection> getRetweetedUsersByTweetId(Long tweetId, Pageable pageable) {
        Tweet tweet = tweetValidationHelper.checkValidTweet(tweetId);
        return userService.getRetweetedUsersByTweet(tweet, pageable);
    }

    @Override
    @Transactional
    public NotificationResponse retweet(Long tweetId) {
        Tweet tweet = tweetValidationHelper.checkValidTweet(tweetId);
        User authUser = userService.getAuthUser();
        Retweet retweet = retweetRepository.isTweetRetweeted(authUser, tweet);
        boolean isRetweeted;

        if (retweet != null) {
            retweetRepository.delete(retweet);
            isRetweeted = false;
        } else {
            retweetRepository.save(new Retweet(authUser, tweet));
            isRetweeted = true;
        }
        updateTweetCountProducer.sendUpdateTweetCountEvent(authUser.getId(), isRetweeted);
        return tweetServiceHelper.sendNotification(NotificationType.RETWEET, isRetweeted, tweet.getAuthor().getId(), authUser.getId(), tweetId);
    }
}
