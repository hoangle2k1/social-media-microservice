package com.gmail.producer;

import com.gmail.event.UpdateUserEvent;
import com.gmail.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.gmail.constants.KafkaTopicConstants.UPDATE_USER_TOPIC;

@Component
@RequiredArgsConstructor
public class UpdateUserProducer {

    private final KafkaTemplate<String, UpdateUserEvent> kafkaTemplate;

    public void sendUpdateUserEvent(User user) {
        kafkaTemplate.send(UPDATE_USER_TOPIC, toUpdateUserEvent(user));
    }

    private static UpdateUserEvent toUpdateUserEvent(User user) {
        return UpdateUserEvent.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .about(user.getAbout())
                .avatar(user.getAvatar())
                .privateProfile(user.isPrivateProfile())
                .active(user.isActive())
                .mutedDirectMessages(user.isMutedDirectMessages())
                .build();
    }
}
