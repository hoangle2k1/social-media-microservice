package com.gmail.repository.projection;

public interface CommonUserProjection {
    Long getId();
    String getFullName();
    String getUsername();
    String getAvatar();
    boolean isPrivateProfile();
}
