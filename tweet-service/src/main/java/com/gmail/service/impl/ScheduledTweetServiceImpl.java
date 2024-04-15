package com.gmail.service.impl;

import com.gmail.constants.ErrorMessage;
import com.gmail.exception.ApiRequestException;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.repository.TweetRepository;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.service.ScheduledTweetService;
import com.gmail.service.TweetService;
import com.gmail.service.UserService;
import com.gmail.service.util.TweetServiceHelper;
import com.gmail.service.util.TweetValidationHelper;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledTweetServiceImpl implements ScheduledTweetService {

    private final TweetRepository tweetRepository;
    private final TweetService tweetService;
    private final TweetServiceHelper tweetServiceHelper;
    private final TweetValidationHelper tweetValidationHelper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<TweetProjection> getScheduledTweets(Pageable pageable) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return tweetRepository.getScheduledTweets(authUserId, pageable);
    }

    @Override
    @Transactional
    public TweetProjection createScheduledTweet(Tweet tweet) {
        tweetValidationHelper.checkTweetTextLength(tweet.getText());
        User authUser = userService.getAuthUser();
        tweet.setAuthor(authUser);
        tweetServiceHelper.parseMetadataFromURL(tweet);
        tweetRepository.save(tweet);
        return tweetService.getTweetById(tweet.getId());
    }

    @Override
    @Transactional
    public TweetProjection updateScheduledTweet(Tweet tweetInfo) {
        Tweet tweet = tweetRepository.findById(tweetInfo.getId())
                .orElseThrow(() -> new ApiRequestException(ErrorMessage.TWEET_NOT_FOUND, HttpStatus.NOT_FOUND));
        tweetValidationHelper.checkTweetTextLength(tweetInfo.getText());
        tweet.setText(tweetInfo.getText());
        tweet.setImages(tweetInfo.getImages());
        return tweetService.getTweetById(tweet.getId());
    }

    @Override
    @Transactional
    public String deleteScheduledTweets(List<Long> tweetsIds) {
        tweetsIds.forEach(tweetService::deleteTweet);
        return "Scheduled tweets deleted.";
    }
}
