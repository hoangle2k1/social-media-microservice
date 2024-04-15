package com.gmail.feign;

import com.gmail.configuration.FeignConfiguration;
import com.gmail.dto.request.IdsRequest;
import com.gmail.dto.response.notification.NotificationTweetResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.gmail.constants.FeignConstants.TWEET_SERVICE;
import static com.gmail.constants.PathConstants.*;

@CircuitBreaker(name = TWEET_SERVICE)
@FeignClient(name = TWEET_SERVICE, path = API_V1_TWEETS, configuration = FeignConfiguration.class)
public interface TweetClient {

    @GetMapping(TWEET_ID)
    TweetResponse getTweetById(@PathVariable("tweetId") Long tweetId);

    @GetMapping(NOTIFICATION_TWEET_ID)
    NotificationTweetResponse getNotificationTweet(@PathVariable("tweetId") Long tweetId);

    @PostMapping(IDS)
    List<TweetResponse> getTweetsByIds(@RequestBody IdsRequest idsRequest);
}
