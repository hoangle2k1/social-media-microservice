package com.gmail.service;

import com.gmail.repository.projection.UserChatProjection;
import com.gmail.repository.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatParticipantService {

    UserProjection getParticipant(Long participantId, Long chatId);

    String leaveFromConversation(Long participantId, Long chatId);

    Page<UserChatProjection> searchUsersByUsername(String username, Pageable pageable);
}
