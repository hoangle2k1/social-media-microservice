package com.gmail.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.dto.response.tweet.TweetAdditionalInfoUserResponse;
import com.gmail.enums.ReplyType;
import lombok.Data;

@Data
public class TweetAdditionalInfoResponse {
    private String text;
    private ReplyType replyType;
    private Long addressedTweetId;
    private TweetAdditionalInfoUserResponse author;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
