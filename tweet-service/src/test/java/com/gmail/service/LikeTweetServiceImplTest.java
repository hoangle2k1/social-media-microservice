package com.gmail.service;

import com.gmail.constants.ErrorMessage;
import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.enums.NotificationType;
import com.gmail.exception.ApiRequestException;
import com.gmail.TweetServiceTestHelper;
import com.gmail.dto.request.NotificationRequest;
import com.gmail.feign.NotificationClient;
import com.gmail.model.LikeTweet;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.repository.LikeTweetRepository;
import com.gmail.repository.TweetRepository;
import com.gmail.repository.projection.LikeTweetProjection;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.impl.LikeTweetServiceImpl;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LikeTweetServiceImplTest extends AbstractAuthTest {

    @Autowired
    private LikeTweetServiceImpl likeTweetService;

    @MockBean
    private LikeTweetRepository likeTweetRepository;

    @MockBean
    private TweetRepository tweetRepository;

    @MockBean
    private NotificationClient notificationClient;

    @MockBean
    private UserService userService;

    private static final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private static Tweet tweet;
    private static User authUser;

    @Before
    public void setUp() {
        super.setUp();
        tweet = new Tweet();
        tweet.setDeleted(false);
        authUser = new User();
        authUser.setId(TestConstants.USER_ID);
        tweet.setAuthor(authUser);
    }

    @Test
    public void getUserLikedTweets() {
        Page<LikeTweetProjection> likeTweet = new PageImpl<>(createMockLikeTweetProjectionList(), pageable, 20);
        when(userService.getUserById(TestConstants.USER_ID)).thenReturn(Optional.ofNullable(authUser));
        when(likeTweetRepository.getUserLikedTweets(TestConstants.USER_ID, pageable)).thenReturn(likeTweet);
        assertEquals(likeTweet, likeTweetService.getUserLikedTweets(TestConstants.USER_ID, pageable));
        verify(likeTweetRepository, times(1)).getUserLikedTweets(TestConstants.USER_ID, pageable);
    }

    @Test
    public void getUserLikedTweets_ShouldUserIdNotFound() {
        when(userService.getUserById(TestConstants.USER_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.getUserLikedTweets(TestConstants.USER_ID, pageable));
        assertEquals(String.format(ErrorMessage.USER_ID_NOT_FOUND, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getUserLikedTweets_ShouldUserNotFound() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(userService.getUserById(1L)).thenReturn(Optional.of(authUser));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.getUserLikedTweets(1L, pageable));
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getUserLikedTweets_ShouldUserProfileBLocked() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(userService.getUserById(1L)).thenReturn(Optional.of(authUser));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(false);
        when(userService.isMyProfileBlockedByUser(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.getUserLikedTweets(1L, pageable));
        Assertions.assertEquals(ErrorMessage.USER_PROFILE_BLOCKED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getLikedUsersByTweetId() {
        Page<UserProjection> userProjections = TweetServiceTestHelper.createUserProjections();
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.getLikedUsersByTweet(tweet, pageable)).thenReturn(userProjections);
        assertEquals(userProjections, likeTweetService.getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable));
        verify(tweetRepository, times(1)).findById(TestConstants.TWEET_ID);
        verify(userService, times(1)).getLikedUsersByTweet(tweet, pageable);
    }

    @Test
    public void getLikedUsersByTweetId_ShouldTweetNotFound() {
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable));
        Assertions.assertEquals(ErrorMessage.TWEET_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getLikedUsersByTweetId_ShouldTweetDeleted() {
        tweet.setDeleted(true);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable));
        Assertions.assertEquals(ErrorMessage.TWEET_DELETED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getLikedUsersByTweetId_ShouldUserNotFound() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable));
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getLikedUsersByTweetId_ShouldUserProfileBlocked() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(false);
        when(userService.isMyProfileBlockedByUser(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable));
        Assertions.assertEquals(ErrorMessage.USER_PROFILE_BLOCKED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void likeTweet_ShouldUnlikeTweet() {
        NotificationRequest request = TweetServiceTestHelper.createMockNotificationRequest(NotificationType.LIKE, false);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.getAuthUser()).thenReturn(authUser);
        when(likeTweetRepository.getLikedTweet(authUser, tweet)).thenReturn(new LikeTweet());
        when(notificationClient.sendTweetNotification(request)).thenReturn(new NotificationResponse());
        assertEquals(new NotificationResponse(), likeTweetService.likeTweet(TestConstants.TWEET_ID));
        verify(tweetRepository, times(1)).findById(TestConstants.TWEET_ID);
        verify(likeTweetRepository, times(1)).getLikedTweet(authUser, tweet);
        verify(likeTweetRepository, times(1)).delete(any());
        verify(notificationClient, times(1)).sendTweetNotification(request);
    }

    @Test
    public void likeTweet_ShouldLikeTweet() {
        NotificationRequest request = TweetServiceTestHelper.createMockNotificationRequest(NotificationType.LIKE, true);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.getAuthUser()).thenReturn(authUser);
        when(likeTweetRepository.getLikedTweet(authUser, tweet)).thenReturn(null);
        when(notificationClient.sendTweetNotification(request)).thenReturn(new NotificationResponse());
        assertEquals(new NotificationResponse(), likeTweetService.likeTweet(TestConstants.TWEET_ID));
        verify(tweetRepository, times(1)).findById(TestConstants.TWEET_ID);
        verify(likeTweetRepository, times(1)).getLikedTweet(authUser, tweet);
        verify(likeTweetRepository, times(1)).save(any());
        verify(notificationClient, times(1)).sendTweetNotification(request);
    }

    @Test
    public void likeTweet_ShouldTweetNotFound() {
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.likeTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.TWEET_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void likeTweet_ShouldTweetDeleted() {
        tweet.setDeleted(true);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.likeTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.TWEET_DELETED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void likeTweet_ShouldUserNotFound() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.likeTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void likeTweet_ShouldUserProfileBlocked() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(false);
        when(userService.isMyProfileBlockedByUser(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> likeTweetService.likeTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.USER_PROFILE_BLOCKED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    private static List<LikeTweetProjection> createMockLikeTweetProjectionList() {
        LikeTweetProjection likeTweetProjection1 = factory.createProjection(
                LikeTweetProjection.class,
                Map.of(
                        "id", 1L,
                        "likeTweetDate", LocalDateTime.now(),
                        "tweetId", TestConstants.TWEET_ID,
                        "tweet", TweetServiceTestHelper.createTweetProjection(false, TweetProjection.class)
                ));
        LikeTweetProjection likeTweetProjection2 = factory.createProjection(
                LikeTweetProjection.class,
                Map.of(
                        "id", 2L,
                        "likeTweetDate", LocalDateTime.now(),
                        "tweetId", TestConstants.TWEET_ID,
                        "tweet", TweetServiceTestHelper.createTweetProjection(false, TweetProjection.class)
                ));
        return Arrays.asList(likeTweetProjection1, likeTweetProjection2);
    }
}
