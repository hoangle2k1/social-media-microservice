package com.gmail.mapper;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.request.IdsRequest;
import com.gmail.dto.response.chat.ChatTweetResponse;
import com.gmail.dto.response.notification.NotificationTweetResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.repository.projection.ChatTweetProjection;
import com.gmail.repository.projection.NotificationTweetProjection;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.service.TweetClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TweetClientMapper {

    private final BasicMapper basicMapper;
    private final TweetClientService tweetClientService;

    public List<TweetResponse> getTweetsByIds(IdsRequest requests) {
        List<TweetProjection> tweets = tweetClientService.getTweetsByIds(requests);
        return basicMapper.convertToResponseList(tweets, TweetResponse.class);
    }

    public HeaderResponse<TweetResponse> getTweetsByUserIds(IdsRequest request, Pageable pageable) {
        Page<TweetProjection> tweets = tweetClientService.getTweetsByUserIds(request, pageable);
        return basicMapper.getHeaderResponse(tweets, TweetResponse.class);
    }

    public TweetResponse getTweetById(Long tweetId) {
        TweetProjection tweet = tweetClientService.getTweetById(tweetId);
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }

    public NotificationTweetResponse getNotificationTweet(Long tweetId) {
        NotificationTweetProjection tweet = tweetClientService.getNotificationTweet(tweetId);
        return basicMapper.convertToResponse(tweet, NotificationTweetResponse.class);
    }

    public Boolean isTweetExists(Long tweetId) {
        return tweetClientService.isTweetExists(tweetId);
    }

    public Long getTweetCountByText(String text) {
        return tweetClientService.getTweetCountByText(text);
    }

    public ChatTweetResponse getChatTweet(Long tweetId) {
        ChatTweetProjection tweet = tweetClientService.getChatTweet(tweetId);
        return basicMapper.convertToResponse(tweet, ChatTweetResponse.class);
    }
}
