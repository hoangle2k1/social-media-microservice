package com.gmail.producer;

import com.gmail.event.FollowRequestUserEvent;
import com.gmail.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.gmail.constants.KafkaTopicConstants.FOLLOW_REQUEST_USER_TOPIC;
import static com.gmail.constants.PathConstants.AUTH_USER_ID_HEADER;

@Component
@RequiredArgsConstructor
public class FollowRequestUserProducer {

    private final KafkaTemplate<String, FollowRequestUserEvent> kafkaTemplate;

    public void sendFollowRequestUserEvent(User user, Long authUserId, boolean hasUserFollowRequest) {
        ProducerRecord<String, FollowRequestUserEvent> producerRecord = new ProducerRecord<>(
                FOLLOW_REQUEST_USER_TOPIC,
                toFollowRequestUserEvent(user, hasUserFollowRequest)
        );
        producerRecord.headers().add(AUTH_USER_ID_HEADER, authUserId.toString().getBytes());
        kafkaTemplate.send(producerRecord);
    }

    private static FollowRequestUserEvent toFollowRequestUserEvent(User user, boolean hasUserFollowRequest) {
        return FollowRequestUserEvent.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .about(user.getAbout())
                .avatar(user.getAvatar())
                .privateProfile(user.isPrivateProfile())
                .active(user.isActive())
                .mutedDirectMessages(user.isMutedDirectMessages())
                .userFollowRequest(hasUserFollowRequest)
                .build();
    }
}
