package com.gmail.consumer;

import com.gmail.event.BlockUserEvent;
import com.gmail.event.FollowRequestUserEvent;
import com.gmail.event.FollowUserEvent;
import com.gmail.event.UpdateUserEvent;
import com.gmail.service.UserServiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static com.gmail.constants.KafkaTopicConstants.*;
import static com.gmail.constants.PathConstants.AUTH_USER_ID_HEADER;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final UserServiceHandler userServiceHandler;

    @KafkaListener(topics = UPDATE_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userUpdateListener(UpdateUserEvent userEvent) {
        userServiceHandler.handleUpdateUser(userEvent);
    }

    @KafkaListener(topics = BLOCK_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userBlockListener(BlockUserEvent userEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userServiceHandler.handleBlockUser(userEvent, authId);
    }

    @KafkaListener(topics = FOLLOW_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userFollowListener(FollowUserEvent userEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userServiceHandler.handleFollowUser(userEvent, authId);
    }

    @KafkaListener(topics = FOLLOW_REQUEST_USER_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void userFollowRequestListener(FollowRequestUserEvent userEvent, @Header(AUTH_USER_ID_HEADER) String authId) {
        userServiceHandler.handleFollowUserRequest(userEvent, authId);
    }
}
