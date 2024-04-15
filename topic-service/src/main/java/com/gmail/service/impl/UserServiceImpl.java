package com.gmail.service.impl;

import com.gmail.constants.ErrorMessage;
import com.gmail.event.BlockUserEvent;
import com.gmail.event.FollowUserEvent;
import com.gmail.event.UpdateUserEvent;
import com.gmail.event.UserEvent;
import com.gmail.exception.ApiRequestException;
import com.gmail.model.User;
import com.gmail.repository.UserRepository;
import com.gmail.service.UserService;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getAuthUser() {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.findById(authUserId)
                .orElseThrow(() -> new ApiRequestException(ErrorMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED));
    }

    @Override
    public void validateUserProfile(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();

        if (!userRepository.isUserExists(userId)) {
            throw new ApiRequestException(String.format(ErrorMessage.USER_ID_NOT_FOUND, userId), HttpStatus.NOT_FOUND);
        }

        if (!userId.equals(authUserId)) {
            if (userRepository.isUserBlocked(userId, authUserId)) {
                throw new ApiRequestException(ErrorMessage.USER_PROFILE_BLOCKED, HttpStatus.BAD_REQUEST);
            }
            if (!userRepository.isUserHavePrivateProfile(userId, authUserId)) {
                throw new ApiRequestException(ErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }
    }

    @Override
    @Transactional
    public void handleUpdateUser(UpdateUserEvent updateUserEvent) {
        userRepository.findById(updateUserEvent.getId())
                .map(user -> {
                    user.setUsername(updateUserEvent.getUsername());
                    user.setFullName(updateUserEvent.getFullName());
                    user.setPrivateProfile(updateUserEvent.isPrivateProfile());
                    return user;
                })
                .orElseGet(() -> createUser(updateUserEvent));
    }

    @Override
    @Transactional
    public void handleBlockUser(BlockUserEvent blockUserEvent, String authId) {
        User user = userRepository.findById(blockUserEvent.getId())
                .orElseGet(() -> createUser(blockUserEvent));
        User authUser = userRepository.findById(parseLong(authId)).get();

        if (blockUserEvent.isUserBlocked()) {
            authUser.getUserBlockedList().add(user);
            authUser.getFollowers().remove(user);
            authUser.getFollowing().remove(user);
        } else {
            authUser.getUserBlockedList().remove(user);
        }
    }

    @Override
    @Transactional
    public void handleFollowUser(FollowUserEvent followUserEvent, String authId) {
        User user = userRepository.findById(followUserEvent.getId())
                .orElseGet(() -> createUser(followUserEvent));
        User authUser = userRepository.findById(parseLong(authId)).get();

        if (followUserEvent.isUserFollow()) {
            authUser.getFollowers().add(user);
        } else {
            authUser.getFollowers().remove(user);
        }
    }

    private User createUser(UserEvent userEvent) {
        User newUser = new User();
        newUser.setId(userEvent.getId());
        newUser.setUsername(userEvent.getUsername());
        newUser.setFullName(userEvent.getFullName());
        newUser.setPrivateProfile(userEvent.isPrivateProfile());
        return userRepository.save(newUser);
    }
}
