package com.gmail.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.dto.response.user.CommonUserResponse;
import lombok.Data;

@Data
public class ListResponse {
    private Long id;
    private String listName;
    private String description;
    private String altWallpaper;
    private String wallpaper;
    private CommonUserResponse listOwner;

    @JsonProperty("isFollower")
    private boolean isFollower;

    @JsonProperty("isListPinned")
    private boolean isListPinned;
}
