package com.gmail.service.impl;

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

import java.util.List;

import static com.gmail.constants.ErrorMessage.USER_ID_BLOCKED;
import static com.gmail.constants.ErrorMessage.USER_NOT_FOUND;
import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getAuthUser() {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.findById(authUserId)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.UNAUTHORIZED));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<User> searchListMembersByUsername(String username) {
        return userRepository.searchListMembersByUsername(username);
    }

    @Override
    public void checkUserIsBlocked(Long userId, Long supposedBlockedUserId) {
        if (isUserBlocked(userId, supposedBlockedUserId)) {
            throw new ApiRequestException(String.format(USER_ID_BLOCKED, supposedBlockedUserId), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void checkIsPrivateUserProfile(Long userId, Long authUserId) {
        if (isUserHavePrivateProfile(userId, authUserId)) {
            throw new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean isUserBlocked(Long userId, Long supposedBlockedUserId) {
        return userRepository.isUserBlocked(userId, supposedBlockedUserId);
    }

    @Override
    public boolean isUserHavePrivateProfile(Long userId, Long authUserId) {
        return !userRepository.isUserHavePrivateProfile(userId, authUserId);
    }

    @Override
    @Transactional
    public void handleUpdateUser(UpdateUserEvent updateUserEvent) {
        userRepository.findById(updateUserEvent.getId())
                .map(user -> {
                    user.setUsername(updateUserEvent.getUsername());
                    user.setFullName(updateUserEvent.getFullName());
                    user.setAbout(updateUserEvent.getAbout());
                    user.setAvatar(updateUserEvent.getAvatar());
                    user.setPrivateProfile(updateUserEvent.isPrivateProfile());
                    user.setActive(updateUserEvent.isActive());

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
        newUser.setAbout(userEvent.getAbout());
        newUser.setAvatar(userEvent.getAvatar());
        newUser.setPrivateProfile(userEvent.isPrivateProfile());
        newUser.setActive(userEvent.isActive());
        return userRepository.save(newUser);
    }
}
