package com.gmail.service.impl;

import com.gmail.dto.response.notification.NotificationListResponse;
import com.gmail.dto.response.tweet.TweetListResponse;
import com.gmail.dto.response.user.CommonUserResponse;
import com.gmail.mapper.BasicMapper;
import com.gmail.ListsServiceTestHelper;
import com.gmail.repository.ListsRepository;
import com.gmail.repository.projection.NotificationListProjection;
import com.gmail.repository.projection.TweetListProjection;
import com.gmail.service.ListsClientService;
import com.gmail.service.UserService;
import com.gmail.util.TestConstants;
import com.gmail.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ListsClientServiceImplTest {

    @Autowired
    private ListsClientService listsClientService;

    @MockBean
    private ListsRepository listsRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private BasicMapper basicMapper;

    private static final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() {
        TestUtil.mockAuthenticatedUserId();
    }

    @Test
    public void getNotificationList() {
        NotificationListProjection notificationList = factory.createProjection(
                NotificationListProjection.class,
                Map.of("id", 1L, "name", TestConstants.LIST_NAME));
        NotificationListResponse listResponse = new NotificationListResponse();
        listResponse.setId(1L);
        listResponse.setListName(TestConstants.LIST_NAME);
        when(listsRepository.getListById(TestConstants.LIST_ID, NotificationListProjection.class)).thenReturn(notificationList);
        when(basicMapper.convertToResponse(notificationList, NotificationListResponse.class)).thenReturn(listResponse);
        Assertions.assertEquals(listResponse, listsClientService.getNotificationList(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, NotificationListProjection.class);
        verify(basicMapper, times(1)).convertToResponse(notificationList, NotificationListResponse.class);
    }

    @Test
    public void getTweetList() {
        TweetListResponse tweetListResponse = new TweetListResponse();
        tweetListResponse.setId(TestConstants.LIST_ID);
        tweetListResponse.setListName(TestConstants.LIST_NAME);
        tweetListResponse.setAltWallpaper(TestConstants.LIST_ALT_WALLPAPER);
        tweetListResponse.setWallpaper("");
        tweetListResponse.setListOwner(new CommonUserResponse());
        tweetListResponse.setPrivate(false);
        tweetListResponse.setMembersSize(1L);
        TweetListProjection tweetListProjection = ListsServiceTestHelper.mockTweetListProjection(TestConstants.LIST_USER_ID);
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class)).thenReturn(Optional.of(tweetListProjection));
        when(userService.isUserBlocked(tweetListProjection.getListOwner().getId(), TestConstants.USER_ID)).thenReturn(false);
        when(userService.isUserHavePrivateProfile(tweetListProjection.getListOwner().getId(), TestConstants.USER_ID)).thenReturn(false);
        when(basicMapper.convertToResponse(tweetListProjection, TweetListResponse.class)).thenReturn(tweetListResponse);
        Assertions.assertEquals(tweetListResponse, listsClientService.getTweetList(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class);
        verify(userService, times(1)).isUserBlocked(tweetListProjection.getListOwner().getId(), TestConstants.USER_ID);
        verify(basicMapper, times(1)).convertToResponse(tweetListProjection, TweetListResponse.class);
    }

    @Test
    public void getTweetList_shouldReturnEmptyTweetListResponse() {
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class)).thenReturn(Optional.empty());
        Assertions.assertEquals(new TweetListResponse(), listsClientService.getTweetList(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class);
    }

    @Test
    public void getTweetList_shouldUserBlockAndReturnEmptyTweetListResponse() {
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class))
                .thenReturn(Optional.of(ListsServiceTestHelper.mockTweetListProjection(TestConstants.LIST_USER_ID)));
        when(userService.isUserBlocked(TestConstants.LIST_USER_ID, TestConstants.USER_ID)).thenReturn(true);
        Assertions.assertEquals(new TweetListResponse(), listsClientService.getTweetList(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class);
        verify(userService, times(1)).isUserBlocked(TestConstants.LIST_USER_ID, TestConstants.USER_ID);
    }

    @Test
    public void getTweetList_shouldUserHavePrivateProfileAndReturnEmptyTweetListResponse() {
        when(listsRepository.getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class))
                .thenReturn(Optional.of(ListsServiceTestHelper.mockTweetListProjection(1L)));
        when(userService.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(false);
        when(userService.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(true);
        Assertions.assertEquals(new TweetListResponse(), listsClientService.getTweetList(TestConstants.LIST_ID));
        verify(listsRepository, times(1)).getListById(TestConstants.LIST_ID, TestConstants.USER_ID, TweetListProjection.class);
        verify(userService, times(1)).isUserBlocked(1L, TestConstants.USER_ID);
        verify(userService, times(1)).isUserHavePrivateProfile(1L, TestConstants.USER_ID);
    }
}
