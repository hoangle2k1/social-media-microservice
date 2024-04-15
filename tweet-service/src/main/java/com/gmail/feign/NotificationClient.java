package com.gmail.feign;

import com.gmail.configuration.FeignConfiguration;
import com.gmail.constants.FeignConstants;
import com.gmail.constants.PathConstants;
import com.gmail.dto.request.NotificationRequest;
import com.gmail.dto.response.notification.NotificationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = FeignConstants.NOTIFICATION_SERVICE)
@FeignClient(name = FeignConstants.NOTIFICATION_SERVICE, path = PathConstants.API_V1_NOTIFICATION, configuration = FeignConfiguration.class)
public interface NotificationClient {

    @PostMapping(PathConstants.TWEET)
    NotificationResponse sendTweetNotification(@RequestBody NotificationRequest request);

    @PostMapping(PathConstants.MENTION)
    void sendTweetMentionNotification(@RequestBody NotificationRequest request);

    @GetMapping(PathConstants.TWEET_TWEET_ID)
    void sendTweetNotificationToSubscribers(@PathVariable("tweetId") Long tweetId);
}
