package com.gmail.mapper;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.response.MutedUserResponse;
import com.gmail.repository.projection.MutedUserProjection;
import com.gmail.service.MuteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MuteUserMapper {

    private final BasicMapper basicMapper;
    private final MuteUserService muteUserService;

    public HeaderResponse<MutedUserResponse> getMutedList(Pageable pageable) {
        Page<MutedUserProjection> mutedList = muteUserService.getMutedList(pageable);
        return basicMapper.getHeaderResponse(mutedList, MutedUserResponse.class);
    }

    public Boolean processMutedList(Long userId) {
        return muteUserService.processMutedList(userId);
    }
}
