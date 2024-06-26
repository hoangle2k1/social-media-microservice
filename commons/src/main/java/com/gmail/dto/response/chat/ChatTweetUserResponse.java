package com.gmail.dto.response.chat;

import lombok.Data;

@Data
public class ChatTweetUserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String avatar;
}
