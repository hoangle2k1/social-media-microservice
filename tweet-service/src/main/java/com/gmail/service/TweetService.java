package com.gmail.service;

import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.enums.ReplyType;
import com.gmail.model.Tweet;
import com.gmail.model.TweetImage;
import com.gmail.repository.projection.*;
import com.gmail.repository.projection.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TweetService {

    Page<TweetProjection> getTweets(Pageable pageable);

    TweetProjection getTweetById(Long tweetId);

    Page<TweetUserProjection> getUserTweets(Long userId, Pageable pageable);

    Page<TweetProjection> getUserMediaTweets(Long userId, Pageable pageable);

    List<ProfileTweetImageProjection> getUserTweetImages(Long userId);

    TweetAdditionalInfoProjection getTweetAdditionalInfoById(Long tweetId);

    List<TweetProjection> getRepliesByTweetId(Long tweetId);

    Page<TweetProjection> getQuotesByTweetId(Long tweetId, Pageable pageable);

    Page<TweetProjection> getMediaTweets(Pageable pageable);

    Page<TweetProjection> getTweetsWithVideo(Pageable pageable);

    Page<TweetProjection> getFollowersTweets(Pageable pageable);

    TweetImage uploadTweetImage(MultipartFile file);

    Page<UserProjection> getTaggedImageUsers(Long tweetId, Pageable pageable);

    TweetResponse createNewTweet(Tweet tweet);

    String deleteTweet(Long tweetId);

    Page<TweetProjection> searchTweets(String text, Pageable pageable);

    TweetResponse replyTweet(Long tweetId, Tweet reply);

    TweetResponse quoteTweet(Long tweetId, Tweet quote);

    TweetProjection changeTweetReplyType(Long tweetId, ReplyType replyType);
}
