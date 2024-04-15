package com.gmail.mapper;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.response.UserChatResponse;
import com.gmail.dto.response.user.UserResponse;
import com.gmail.repository.projection.UserChatProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.ChatParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatParticipantMapper {

    private final BasicMapper basicMapper;
    private final ChatParticipantService chatParticipantService;

    public UserResponse getParticipant(Long participantId, Long chatId) {
        UserProjection participant = chatParticipantService.getParticipant(participantId, chatId);
        return basicMapper.convertToResponse(participant, UserResponse.class);
    }

    public String leaveFromConversation(Long participantId, Long chatId) {
        return chatParticipantService.leaveFromConversation(participantId, chatId);
    }

    public HeaderResponse<UserChatResponse> searchParticipantsByUsername(String username, Pageable pageable) {
        Page<UserChatProjection> participants = chatParticipantService.searchUsersByUsername(username, pageable);
        return basicMapper.getHeaderResponse(participants, UserChatResponse.class);
    }
}
