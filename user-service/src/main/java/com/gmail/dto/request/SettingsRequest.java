package com.gmail.dto.request;

import com.gmail.enums.BackgroundColorType;
import com.gmail.enums.ColorSchemeType;
import lombok.Data;

@Data
public class SettingsRequest {
    private String username;
    private String email;
    private String countryCode;
    private Long phone;
    private String country;
    private String gender;
    private String language;
    private boolean mutedDirectMessages;
    private boolean privateProfile;
    private BackgroundColorType backgroundColor;
    private ColorSchemeType colorScheme;
}
