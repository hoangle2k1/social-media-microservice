package com.gmail.service.impl;

import com.gmail.exception.ApiRequestException;
import com.gmail.model.Chat;
import com.gmail.model.ChatParticipant;
import com.gmail.model.User;
import com.gmail.repository.ChatParticipantRepository;
import com.gmail.repository.ChatRepository;
import com.gmail.repository.projection.UserChatProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.ChatParticipantService;
import com.gmail.service.UserService;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.constants.ErrorMessage.CHAT_NOT_FOUND;
import static com.gmail.constants.ErrorMessage.CHAT_PARTICIPANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatParticipantServiceImpl implements ChatParticipantService {

    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserProjection getParticipant(Long participantId, Long chatId) {
        if (!chatRepository.isChatExists(chatId, AuthUtil.getAuthenticatedUserId())) {
            throw new ApiRequestException(CHAT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User user = chatParticipantRepository.getChatParticipant(participantId, chatId)
                .orElseThrow(() -> new ApiRequestException(CHAT_PARTICIPANT_NOT_FOUND, HttpStatus.NOT_FOUND))
                .getUser();
        return userService.getUserProjectionById(user.getId());
    }

    @Override
    @Transactional
    public String leaveFromConversation(Long participantId, Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ApiRequestException(CHAT_NOT_FOUND, HttpStatus.NOT_FOUND));
        ChatParticipant chatParticipant = chatParticipantRepository.getChatParticipant(participantId, chatId)
                .orElseThrow(() -> new ApiRequestException(CHAT_PARTICIPANT_NOT_FOUND, HttpStatus.NOT_FOUND));
        chatParticipant.setLeftChat(true);

        if (chat.getParticipants().stream().allMatch(ChatParticipant::isLeftChat)) {
            chatRepository.delete(chat);
            return "Chat successfully deleted";
        }
        return "Successfully left the chat";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserChatProjection> searchUsersByUsername(String username, Pageable pageable) {
        return userService.searchUsersByUsername(username, pageable);
    }
}
