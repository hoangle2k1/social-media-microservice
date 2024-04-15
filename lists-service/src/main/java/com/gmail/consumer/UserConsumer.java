package com.gmail.consumer;

import com.gmail.event.BlockUserEvent;
import com.gmail.event.FollowUserEvent;
import com.gmail.event.UpdateUserEvent;
import com.gmail.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static com.gmail.constants.KafkaTopicConstants.*;
import static com.gmail.constants.PathConstants.AUTH_USER_ID_HEADER;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @KafkaListener(topics = UPDATE_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userUpdateListener(UpdateUserEvent updateUserEvent) {
        userService.handleUpdateUser(updateUserEvent);
    }

    @KafkaListener(topics = BLOCK_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userBlockListener(BlockUserEvent blockUserEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userService.handleBlockUser(blockUserEvent, authId);
    }

    @KafkaListener(topics = FOLLOW_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userFollowListener(FollowUserEvent followUserEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userService.handleFollowUser(followUserEvent, authId);
    }
}
