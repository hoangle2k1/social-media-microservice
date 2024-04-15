package com.gmail.service;

import com.gmail.event.BlockUserEvent;
import com.gmail.event.FollowUserEvent;
import com.gmail.event.UpdateUserEvent;
import com.gmail.model.User;

public interface UserService {

    User getAuthUser();

    void validateUserProfile(Long userId);

    void handleUpdateUser(UpdateUserEvent updateUserEvent);

    void handleBlockUser(BlockUserEvent blockUserEvent, String authId);

    void handleFollowUser(FollowUserEvent followUserEvent, String authId);
}
