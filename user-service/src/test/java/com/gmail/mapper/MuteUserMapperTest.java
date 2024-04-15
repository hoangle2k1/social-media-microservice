package com.gmail.mapper;

import com.gmail.UserServiceTestHelper;
import com.gmail.repository.projection.MutedUserProjection;
import com.gmail.service.MuteUserService;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MuteUserMapperTest extends AbstractAuthTest {

    @Autowired
    private MuteUserMapper muteUserMapper;

    @MockBean
    private MuteUserService muteUserService;

    @Test
    public void getMutedList() {
        Page<MutedUserProjection> mutedUserProjections = UserServiceTestHelper.createMutedUserProjections();
        when(muteUserService.getMutedList(pageable)).thenReturn(mutedUserProjections);
        muteUserMapper.getMutedList(pageable);
        verify(muteUserService, times(1)).getMutedList(pageable);
    }

    @Test
    public void processMutedList() {
        when(muteUserService.processMutedList(TestConstants.USER_ID)).thenReturn(true);
        assertTrue(muteUserMapper.processMutedList(TestConstants.USER_ID));
        verify(muteUserService, times(1)).processMutedList(TestConstants.USER_ID);
    }
}
