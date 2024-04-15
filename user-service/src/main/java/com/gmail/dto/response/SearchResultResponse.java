package com.gmail.dto.response;

import com.gmail.dto.response.user.CommonUserResponse;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultResponse {
    private String text;
    private Long tweetCount;
    private List<String> tags;
    private List<CommonUserResponse> users;
}
