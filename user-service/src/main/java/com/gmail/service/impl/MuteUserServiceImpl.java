package com.gmail.service.impl;

import com.gmail.repository.MuteUserRepository;
import com.gmail.repository.projection.MutedUserProjection;
import com.gmail.service.AuthenticationService;
import com.gmail.service.MuteUserService;
import com.gmail.service.util.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MuteUserServiceImpl implements MuteUserService {

    private final MuteUserRepository muteUserRepository;
    private final AuthenticationService authenticationService;
    private final UserServiceHelper userServiceHelper;

    @Override
    public Page<MutedUserProjection> getMutedList(Pageable pageable) {
        Long authUserId = authenticationService.getAuthenticatedUserId();
        return muteUserRepository.getUserMuteListById(authUserId, pageable);
    }

    @Override
    @Transactional
    public Boolean processMutedList(Long userId) {
        userServiceHelper.checkIsUserExist(userId);
        Long authUserId = authenticationService.getAuthenticatedUserId();
        boolean isUserMuted = muteUserRepository.isUserMuted(authUserId, userId);

        if (isUserMuted) {
            muteUserRepository.unmuteUser(authUserId, userId);
            return false;
        } else {
            muteUserRepository.muteUser(authUserId, userId);
            return true;
        }
    }
}
