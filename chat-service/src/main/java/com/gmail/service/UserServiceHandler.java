package com.gmail.service;

import com.gmail.event.BlockUserEvent;
import com.gmail.event.FollowRequestUserEvent;
import com.gmail.event.FollowUserEvent;
import com.gmail.event.UpdateUserEvent;

public interface UserServiceHandler {

    void handleUpdateUser(UpdateUserEvent updateUserEvent);

    void handleBlockUser(BlockUserEvent blockUserEvent, String authId);

    void handleFollowUser(FollowUserEvent followUserEvent, String authId);

    void handleFollowUserRequest(FollowRequestUserEvent followRequestUserEvent, String authId);
}
