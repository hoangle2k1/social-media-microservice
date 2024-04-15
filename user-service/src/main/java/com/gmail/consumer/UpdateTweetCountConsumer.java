package com.gmail.consumer;

import com.gmail.event.UpdateTweetCountEvent;
import com.gmail.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static com.gmail.constants.KafkaTopicConstants.*;
import static com.gmail.constants.PathConstants.AUTH_USER_ID_HEADER;

@Component
@RequiredArgsConstructor
public class UpdateTweetCountConsumer {

    private final UserService userService;

    @KafkaListener(topics = UPDATE_USER_TWEETS_COUNT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void updateTweetCountListener(UpdateTweetCountEvent tweetCountEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userService.handleUpdateTweetCount(tweetCountEvent, authId);
    }

    @KafkaListener(topics = UPDATE_USER_LIKES_COUNT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void updateLikeTweetCountListener(UpdateTweetCountEvent tweetCountEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userService.handleUpdateLikeTweetCount(tweetCountEvent, authId);
    }

    @KafkaListener(topics = UPDATE_USER_MEDIA_COUNT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void updateMediaTweetCountListener(UpdateTweetCountEvent tweetCountEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userService.handleUpdateMediaTweetCount(tweetCountEvent, authId);
    }
}
