package com.gmail.mapper;

import com.gmail.dto.request.ChatMessageRequest;
import com.gmail.dto.request.MessageWithTweetRequest;
import com.gmail.dto.response.ChatMessageResponse;
import com.gmail.model.ChatMessage;
import com.gmail.repository.projection.ChatMessageProjection;
import com.gmail.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatMessageMapper {

    private final BasicMapper basicMapper;
    private final ChatMessageService chatMessageService;

    public List<ChatMessageResponse> getChatMessages(Long chatId) {
        List<ChatMessageProjection> chatMessages = chatMessageService.getChatMessages(chatId);
        return basicMapper.convertToResponseList(chatMessages, ChatMessageResponse.class);
    }

    public Long readChatMessages(Long chatId) {
        return chatMessageService.readChatMessages(chatId);
    }

    public Map<Long, ChatMessageResponse> addMessage(ChatMessageRequest request) {
        Map<Long, ChatMessageProjection> messages = chatMessageService.addMessage(
                basicMapper.convertToResponse(request, ChatMessage.class), request.getChatId());
        return getChatMessageResponse(messages);
    }

    public Map<Long, ChatMessageResponse> addMessageWithTweet(MessageWithTweetRequest request) {
        Map<Long, ChatMessageProjection> messages = chatMessageService.addMessageWithTweet(
                request.getText(), request.getTweetId(), request.getUsersIds());
        return getChatMessageResponse(messages);
    }

    private Map<Long, ChatMessageResponse> getChatMessageResponse(Map<Long, ChatMessageProjection> messages) {
        Map<Long, ChatMessageResponse> messagesResponse = new HashMap<>();
        messages.forEach((userId, messageProjection) -> {
            ChatMessageResponse messageResponse = basicMapper.convertToResponse(messageProjection, ChatMessageResponse.class);
            messagesResponse.put(userId, messageResponse);
        });
        return messagesResponse;
    }
}
