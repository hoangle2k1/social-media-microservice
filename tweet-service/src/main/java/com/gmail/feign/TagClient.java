package com.gmail.feign;

import com.gmail.configuration.FeignConfiguration;
import com.gmail.constants.FeignConstants;
import com.gmail.constants.PathConstants;
import com.gmail.dto.request.TweetTextRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = FeignConstants.TAG_SERVICE)
@FeignClient(value = FeignConstants.TAG_SERVICE, path = PathConstants.API_V1_TAGS, configuration = FeignConfiguration.class)
public interface TagClient {

    @PostMapping(PathConstants.PARSE_TWEET_ID)
    void parseHashtagsFromText(@PathVariable("tweetId") Long tweetId, @RequestBody TweetTextRequest request);

    @DeleteMapping(PathConstants.DELETE_TWEET_ID)
    void deleteTagsByTweetId(@PathVariable("tweetId") Long tweetId);
}
