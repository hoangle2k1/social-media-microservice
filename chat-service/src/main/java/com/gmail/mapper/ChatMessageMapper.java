package com.gmail.mapper;

import com.gmail.dto.request.ChatMessageRequest;
import com.gmail.dto.request.MessageWithTweetRequest;
import com.gmail.dto.response.ChatMessageResponse;
import com.gmail.model.ChatMessage;
import com.gmail.repository.projection.ChatMessageProjection;
import com.gmail.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatMessageMapper {

    private final BasicMapper basicMapper;
    private final ChatMessageService chatMessageService;

    private final String CHECK_TOXIC_URL = "http://localhost:5001/filter?text=";

    public List<ChatMessageResponse> getChatMessages(Long chatId) {
        List<ChatMessageProjection> chatMessages = chatMessageService.getChatMessages(chatId);
        return basicMapper.convertToResponseList(chatMessages, ChatMessageResponse.class);
    }

    public Long readChatMessages(Long chatId) {
        return chatMessageService.readChatMessages(chatId);
    }

    public Map<Long, ChatMessageResponse> addMessage(ChatMessageRequest request) {
        if (isToxicWord(request.getText())) {
            System.out.println("message contains toxic word");
            request.setText("This text contains hate and offensive language.");
        }
        Map<Long, ChatMessageProjection> messages = chatMessageService.addMessage(
                basicMapper.convertToResponse(request, ChatMessage.class), request.getChatId());
        System.out.println("Add chat message success");
        return getChatMessageResponse(messages);
    }

    public boolean isToxicWord(String text) {
        System.out.println("Check toxic word with text: " + text);
        String url = CHECK_TOXIC_URL + text;

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("Send request");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, headers);
        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        if (response.getBody().equals("1")) return true;
        return false;
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
