package com.gmail.feign;

import com.gmail.configuration.FeignConfiguration;
import com.gmail.dto.response.chat.ChatTweetResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.gmail.constants.FeignConstants.TWEET_SERVICE;
import static com.gmail.constants.PathConstants.*;

@CircuitBreaker(name = TWEET_SERVICE)
@FeignClient(name = TWEET_SERVICE, path = API_V1_TWEETS, configuration = FeignConfiguration.class)
public interface TweetClient {

    @GetMapping(ID_TWEET_ID)
    Boolean isTweetExists(@PathVariable("tweetId") Long tweetId);

    @GetMapping(CHAT_TWEET_ID)
    ChatTweetResponse getChatTweet(@PathVariable("tweetId") Long tweetId);
}
