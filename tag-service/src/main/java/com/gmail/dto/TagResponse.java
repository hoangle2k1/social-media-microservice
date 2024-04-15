package com.gmail.dto;

import lombok.Data;

@Data
public class TagResponse {
    private Long id;
    private String tagName;
    private Long tweetsQuantity;
}
