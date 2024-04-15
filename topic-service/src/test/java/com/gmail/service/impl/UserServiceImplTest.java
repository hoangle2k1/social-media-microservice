package com.gmail.service.impl;

import com.gmail.constants.ErrorMessage;
import com.gmail.event.BlockUserEvent;
import com.gmail.event.FollowUserEvent;
import com.gmail.event.UpdateUserEvent;
import com.gmail.event.UserEvent;
import com.gmail.exception.ApiRequestException;
import com.gmail.TopicTestHelper;
import com.gmail.model.User;
import com.gmail.repository.UserRepository;
import com.gmail.service.UserService;
import com.gmail.util.TestUtil;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        TestUtil.mockAuthenticatedUserId();
    }

    @Test
    public void getAuthUser() {
        User authUser = TopicTestHelper.mockAuthUser();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertEquals(authUser, userService.getAuthUser());
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    public void getAuthUser_shouldUserNotFound() {
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userService.getAuthUser());
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    public void validateUserProfile_shouldUserIdNotFound() {
        when(userRepository.isUserExists(1L)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userService.validateUserProfile(1L));
        assertEquals(String.format(ErrorMessage.USER_ID_NOT_FOUND, 1L), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void validateUserProfile_shouldUserProfileBlocked() {
        when(userRepository.isUserExists(1L)).thenReturn(true);
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userService.validateUserProfile(1L));
        Assertions.assertEquals(ErrorMessage.USER_PROFILE_BLOCKED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void validateUserProfile_shouldUserHavePrivateProfile() {
        when(userRepository.isUserExists(1L)).thenReturn(true);
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(false);
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userService.validateUserProfile(1L));
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void handleUpdateUser_updateUser() {
        User authUser = TopicTestHelper.mockAuthUser();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertFalse(authUser.isPrivateProfile());
        userService.handleUpdateUser(mockUpdateUserEvent());
        assertTrue(authUser.isPrivateProfile());
    }

    @Test
    public void handleUpdateUser_createUser() {
        UpdateUserEvent updateUserEvent = mockUpdateUserEvent();
        User user = mockCreateUser(updateUserEvent);
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.empty());
        userService.handleUpdateUser(updateUserEvent);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void handleBlockUser_blockUser() {
        BlockUserEvent blockUserEvent = mockBlockUserEvent(true);
        User user = mockCreateUser(blockUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userService.handleBlockUser(blockUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getUserBlockedList().contains(user));
    }

    @Test
    public void handleBlockUser_unblockUser() {
        BlockUserEvent blockUserEvent = mockBlockUserEvent(false);
        User user = mockCreateUser(blockUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        authUser.setUserBlockedList(new HashSet<>(Set.of(user)));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userService.handleBlockUser(blockUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getUserBlockedList().isEmpty());
    }

    @Test
    public void handleFollowUser_followUser() {
        FollowUserEvent followUserEvent = mockFollowUserEvent(true);
        User user = mockCreateUser(followUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userService.handleFollowUser(followUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getFollowers().contains(user));
    }

    @Test
    public void handleFollowUser_unfollowUser() {
        FollowUserEvent followUserEvent = mockFollowUserEvent(false);
        User user = mockCreateUser(followUserEvent);
        User authUser = TopicTestHelper.mockAuthUser();
        authUser.setFollowers(new HashSet<>(Set.of(user)));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        userService.handleFollowUser(followUserEvent, TestConstants.USER_ID.toString());
        assertTrue(authUser.getFollowers().isEmpty());
    }

    private static UpdateUserEvent mockUpdateUserEvent() {
        return UpdateUserEvent.builder()
                .id(TestConstants.USER_ID)
                .fullName(TestConstants.FULL_NAME)
                .username(TestConstants.USERNAME)
                .privateProfile(true)
                .build();
    }

    private static BlockUserEvent mockBlockUserEvent(boolean userBlocked) {
        return BlockUserEvent.builder()
                .id(TestConstants.USER_ID)
                .fullName(TestConstants.FULL_NAME)
                .username(TestConstants.USERNAME)
                .privateProfile(false)
                .userBlocked(userBlocked)
                .build();
    }

    private static FollowUserEvent mockFollowUserEvent(boolean userFollow) {
        return FollowUserEvent.builder()
                .id(TestConstants.USER_ID)
                .fullName(TestConstants.FULL_NAME)
                .username(TestConstants.USERNAME)
                .privateProfile(false)
                .userFollow(userFollow)
                .build();
    }

    private static User mockCreateUser(UserEvent userEvent) {
        User newUser = new User();
        newUser.setId(userEvent.getId());
        newUser.setUsername(userEvent.getUsername());
        newUser.setFullName(userEvent.getFullName());
        newUser.setPrivateProfile(userEvent.isPrivateProfile());
        return newUser;
    }
}
