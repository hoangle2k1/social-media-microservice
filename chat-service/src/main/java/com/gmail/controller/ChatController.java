package com.gmail.controller;

import com.gmail.dto.response.ChatResponse;
import com.gmail.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gmail.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UI_V1_CHAT)
public class ChatController {

    private final ChatMapper chatMapper;

    @GetMapping(CHAT_ID)
    public ResponseEntity<ChatResponse> getChatById(@PathVariable("chatId") Long chatId) {
        return ResponseEntity.ok(chatMapper.getChatById(chatId));
    }

    @GetMapping(USERS)
    public ResponseEntity<List<ChatResponse>> getUserChats() {
        return ResponseEntity.ok(chatMapper.getUserChats());
    }

    @GetMapping(CREATE_USER_ID)
    public ResponseEntity<ChatResponse> createChat(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(chatMapper.createChat(userId));
    }
}
