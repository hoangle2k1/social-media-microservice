package com.gmail.service.impl;

import com.gmail.exception.ApiRequestException;
import com.gmail.model.User;
import com.gmail.repository.UserRepository;
import com.gmail.repository.projection.UserChatProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.UserService;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gmail.constants.ErrorMessage.CHAT_PARTICIPANT_BLOCKED;
import static com.gmail.constants.ErrorMessage.USER_NOT_FOUND;

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
    public UserProjection getUserProjectionById(Long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<UserChatProjection> searchUsersByUsername(String username, Pageable pageable) {
        return userRepository.searchUsersByUsername(username, pageable, UserChatProjection.class);
    }

    @Override
    public List<User> getNotBlockedUsers(List<Long> usersIds) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.getNotBlockedUsers(usersIds, authUserId);
    }

    @Override
    public void isParticipantBlocked(Long authUserId, Long userId) {
        if (isUserBlockedByMyProfile(authUserId) || isMyProfileBlockedByUser(userId)) {
            throw new ApiRequestException(CHAT_PARTICIPANT_BLOCKED, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean isUserBlockedByMyProfile(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isUserBlocked(authUserId, userId);
    }

    @Override
    public boolean isMyProfileBlockedByUser(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isUserBlocked(userId, authUserId);
    }

    @Override
    public boolean isMyProfileWaitingForApprove(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isMyProfileWaitingForApprove(userId, authUserId);
    }

    @Override
    public boolean isUserFollowByOtherUser(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return userRepository.isUserFollowByOtherUser(authUserId, userId);
    }
}
