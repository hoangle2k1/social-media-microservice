package com.gmail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageRequest {
    private Long chatId;
    private String text;
}
