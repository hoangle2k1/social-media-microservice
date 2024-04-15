package com.gmail.service;

import com.gmail.UserServiceTestHelper;
import com.gmail.exception.ApiRequestException;
import com.gmail.repository.MuteUserRepository;
import com.gmail.repository.UserRepository;
import com.gmail.repository.projection.MutedUserProjection;
import com.gmail.service.impl.MuteUserServiceImpl;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import static com.gmail.constants.ErrorMessage.USER_ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MuteUserServiceImplTest extends AbstractAuthTest {

    @Autowired
    private MuteUserServiceImpl muteUserService;

    @MockBean
    private MuteUserRepository muteUserRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        when(authenticationService.getAuthenticatedUserId()).thenReturn(TestConstants.USER_ID);
    }

    @Test
    public void getMutedList_ShouldReturnMutedUserProjections() {
        Page<MutedUserProjection> mutedUserProjection = UserServiceTestHelper.createMutedUserProjections();
        when(muteUserRepository.getUserMuteListById(TestConstants.USER_ID, pageable)).thenReturn(mutedUserProjection);
        assertEquals(mutedUserProjection, muteUserService.getMutedList(pageable));
        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(muteUserRepository, times(1)).getUserMuteListById(TestConstants.USER_ID, pageable);
    }

    @Test
    public void processMutedList_ShouldMuteUser() {
        when(userRepository.isUserExist(TestConstants.USER_ID)).thenReturn(true);
        when(muteUserRepository.isUserMuted(TestConstants.USER_ID, 2L)).thenReturn(false);
        assertTrue(muteUserService.processMutedList(TestConstants.USER_ID));
        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(userRepository, times(1)).isUserExist(TestConstants.USER_ID);
        verify(muteUserRepository, times(1)).isUserMuted(TestConstants.USER_ID, 2L);
        verify(muteUserRepository, times(1)).muteUser(TestConstants.USER_ID, 2L);
    }

    @Test
    public void processMutedList_ShouldUnmuteUser() {
        when(userRepository.isUserExist(TestConstants.USER_ID)).thenReturn(true);
        when(muteUserRepository.isUserMuted(TestConstants.USER_ID, 2L)).thenReturn(true);
        assertFalse(muteUserService.processMutedList(TestConstants.USER_ID));
        verify(authenticationService, times(1)).getAuthenticatedUserId();
        verify(userRepository, times(1)).isUserExist(TestConstants.USER_ID);
        verify(muteUserRepository, times(1)).isUserMuted(TestConstants.USER_ID, 2L);
        verify(muteUserRepository, times(1)).unmuteUser(TestConstants.USER_ID, 2L);
    }

    @Test
    public void processMutedList_ShouldUserNotFound() {
        when(userRepository.isUserExist(TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> muteUserService.processMutedList(TestConstants.USER_ID));
        assertEquals(String.format(USER_ID_NOT_FOUND, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(userRepository, times(1)).isUserExist(TestConstants.USER_ID);
    }
}
