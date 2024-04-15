package com.gmail.consumer;

import com.gmail.constants.KafkaTopicConstants;
import com.gmail.constants.PathConstants;
import com.gmail.event.BlockUserEvent;
import com.gmail.event.FollowUserEvent;
import com.gmail.event.UpdateUserEvent;
import com.gmail.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @KafkaListener(topics = KafkaTopicConstants.UPDATE_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userUpdateListener(UpdateUserEvent updateUserEvent) {
        userService.handleUpdateUser(updateUserEvent);
    }

    @KafkaListener(topics = KafkaTopicConstants.BLOCK_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userBlockListener(BlockUserEvent blockUserEvent, @Header(PathConstants.AUTH_USER_ID_HEADER) String authId) {
        userService.handleBlockUser(blockUserEvent, authId);
    }

    @KafkaListener(topics = KafkaTopicConstants.FOLLOW_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userFollowListener(FollowUserEvent followUserEvent, @Header(PathConstants.AUTH_USER_ID_HEADER) String authId) {
        userService.handleFollowUser(followUserEvent, authId);
    }
}
