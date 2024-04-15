package com.gmail.controller.rest;

import com.gmail.constants.PathConstants;
import com.gmail.constants.WebsocketConstants;
import com.gmail.dto.HeaderResponse;
import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.dto.response.notification.NotificationTweetResponse;
import com.gmail.dto.response.user.UserResponse;
import com.gmail.dto.response.TweetUserResponse;
import com.gmail.feign.WebSocketClient;
import com.gmail.mapper.RetweetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_TWEETS)
public class RetweetController {

    private final RetweetMapper retweetMapper;
    private final WebSocketClient webSocketClient;

    @GetMapping(PathConstants.REPLIES_USER_ID)
    public ResponseEntity<List<TweetUserResponse>> getUserRetweetsAndReplies(@PathVariable("userId") Long userId,
                                                                             @PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<TweetUserResponse> response = retweetMapper.getUserRetweetsAndReplies(userId, pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.TWEET_ID_RETWEETED_USERS)
    public ResponseEntity<List<UserResponse>> getRetweetedUsersByTweetId(@PathVariable("tweetId") Long tweetId,
                                                                         @PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<UserResponse> response = retweetMapper.getRetweetedUsersByTweetId(tweetId, pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.RETWEET_USER_ID_TWEET_ID)
    public ResponseEntity<NotificationTweetResponse> retweet(@PathVariable("userId") Long userId,
                                                             @PathVariable("tweetId") Long tweetId) {
        NotificationResponse notification = retweetMapper.retweet(tweetId);
        webSocketClient.send(WebsocketConstants.TOPIC_USER_UPDATE_TWEET + userId, notification);
        return ResponseEntity.ok(notification.getTweet());
    }
}
