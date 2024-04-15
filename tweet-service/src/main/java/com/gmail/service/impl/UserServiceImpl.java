package com.gmail.service.impl;

import com.gmail.constants.ErrorMessage;
import com.gmail.exception.ApiRequestException;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.repository.UserRepository;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.UserService;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.getUserIdByUsername(username.substring(1));
    }

    @Override
    public Boolean isUserExists(Long userId) {
        return userRepository.isUserExists(userId);
    }

    @Override
    public boolean isUserHavePrivateProfile(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return !userRepository.isUserHavePrivateProfile(userId, authUserId);
    }

    @Override
    public boolean isMyProfileBlockedByUser(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isUserBlocked(userId, authUserId);
    }

    @Override
    public Page<UserProjection> getLikedUsersByTweet(Tweet tweet, Pageable pageable) {
        return userRepository.getLikedUsersByTweet(tweet, pageable);
    }

    @Override
    public Page<UserProjection> getRetweetedUsersByTweet(Tweet tweet, Pageable pageable) {
        return userRepository.getRetweetedUsersByTweet(tweet, pageable);
    }

    @Override
    public Page<UserProjection> getTaggedImageUsers(Tweet tweet, Pageable pageable) {
        return userRepository.getTaggedImageUsers(tweet, pageable);
    }

    public boolean isUserMutedByMyProfile(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isUserMuted(authUserId, userId);
    }

    public boolean isUserBlockedByMyProfile(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isUserBlocked(authUserId, userId);
    }

    public boolean isMyProfileWaitingForApprove(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isMyProfileWaitingForApprove(userId, authUserId);
    }

    public boolean isUserFollowByOtherUser(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isUserFollowByOtherUser(authUserId, userId);
    }
}
