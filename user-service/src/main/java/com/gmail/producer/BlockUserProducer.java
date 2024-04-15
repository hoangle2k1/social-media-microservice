package com.gmail.producer;

import com.gmail.event.BlockUserEvent;
import com.gmail.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.gmail.constants.KafkaTopicConstants.BLOCK_USER_TOPIC;
import static com.gmail.constants.PathConstants.AUTH_USER_ID_HEADER;

@Component
@RequiredArgsConstructor
public class BlockUserProducer {

    private final KafkaTemplate<String, BlockUserEvent> kafkaTemplate;

    public void sendBlockUserEvent(User user, Long authUserId, boolean hasUserBlocked) {
        ProducerRecord<String, BlockUserEvent> producerRecord = new ProducerRecord<>(BLOCK_USER_TOPIC, toBlockUserEvent(user, hasUserBlocked));
        producerRecord.headers().add(AUTH_USER_ID_HEADER, authUserId.toString().getBytes());
        kafkaTemplate.send(producerRecord);
    }

    private static BlockUserEvent toBlockUserEvent(User user, boolean hasUserBlocked) {
        return BlockUserEvent.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .about(user.getAbout())
                .avatar(user.getAvatar())
                .privateProfile(user.isPrivateProfile())
                .active(user.isActive())
                .mutedDirectMessages(user.isMutedDirectMessages())
                .userBlocked(hasUserBlocked)
                .build();
    }
}
