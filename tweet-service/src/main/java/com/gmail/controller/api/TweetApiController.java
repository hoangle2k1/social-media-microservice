package com.gmail.controller.api;

import com.gmail.constants.PathConstants;
import com.gmail.dto.HeaderResponse;
import com.gmail.dto.request.IdsRequest;
import com.gmail.dto.response.chat.ChatTweetResponse;
import com.gmail.dto.response.notification.NotificationTweetResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.mapper.TweetClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_TWEETS)
public class TweetApiController {

    private final TweetClientMapper tweetClientMapper;

    @PostMapping(PathConstants.IDS)
    public List<TweetResponse> getTweetsByIds(@RequestBody IdsRequest requests) {
        return tweetClientMapper.getTweetsByIds(requests);
    }

    @PostMapping(PathConstants.USER_IDS)
    public HeaderResponse<TweetResponse> getTweetsByUserIds(@RequestBody IdsRequest request,
                                                            @SpringQueryMap Pageable pageable) {
        return tweetClientMapper.getTweetsByUserIds(request, pageable);
    }

    @GetMapping(PathConstants.TWEET_ID)
    public TweetResponse getTweetById(@PathVariable("tweetId") Long tweetId) {
        return tweetClientMapper.getTweetById(tweetId);
    }

    @GetMapping(PathConstants.NOTIFICATION_TWEET_ID)
    public NotificationTweetResponse getNotificationTweet(@PathVariable("tweetId") Long tweetId) {
        return tweetClientMapper.getNotificationTweet(tweetId);
    }

    @GetMapping(PathConstants.ID_TWEET_ID)
    public Boolean isTweetExists(@PathVariable("tweetId") Long tweetId) {
        return tweetClientMapper.isTweetExists(tweetId);
    }

    @GetMapping(PathConstants.COUNT_TEXT)
    public Long getTweetCountByText(@PathVariable("text") String text) {
        return tweetClientMapper.getTweetCountByText(text);
    }

    @GetMapping(PathConstants.CHAT_TWEET_ID)
    public ChatTweetResponse getChatTweet(@PathVariable("tweetId") Long tweetId) {
        return tweetClientMapper.getChatTweet(tweetId);
    }
}
