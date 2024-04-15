package com.gmail.service.util;

import com.gmail.constants.ErrorMessage;
import com.gmail.exception.ApiRequestException;
import com.gmail.TweetServiceTestHelper;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.repository.TweetRepository;
import com.gmail.service.UserService;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TweetValidationHelperTest extends AbstractAuthTest {

    @Autowired
    private TweetValidationHelper tweetValidationHelper;

    @MockBean
    private TweetRepository tweetRepository;

    @MockBean
    private UserService userService;

    private static Tweet tweet;

    @Before
    public void setUp() {
        super.setUp();
        tweet = new Tweet();
        User authUser = new User();
        authUser.setId(TestConstants.USER_ID);
        tweet.setAuthor(authUser);
    }

    @Test
    public void checkValidTweet() {
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        assertEquals(tweet, tweetValidationHelper.checkValidTweet(TestConstants.TWEET_ID));
        verify(tweetRepository, times(1)).findById(TestConstants.TWEET_ID);
    }

    @Test
    public void checkValidTweet_ShouldTweetNotFound() {
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> tweetValidationHelper.checkValidTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.TWEET_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void checkValidTweet_ShouldTweetDeleted() {
        User authUser = new User();
        authUser.setId(TestConstants.USER_ID);
        Tweet tweet = new Tweet();
        tweet.setDeleted(true);
        tweet.setAuthor(authUser);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> tweetValidationHelper.checkValidTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.TWEET_DELETED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void checkValidTweet_ShouldUserNotFound() {
        TweetServiceTestHelper.mockAuthenticatedUserId();
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> tweetValidationHelper.checkValidTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void checkValidTweet_ShouldUserProfileBlocked() {
        TweetServiceTestHelper.mockAuthenticatedUserId();
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(TestConstants.USER_ID)).thenReturn(false);
        when(userService.isMyProfileBlockedByUser(TestConstants.USER_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> tweetValidationHelper.checkValidTweet(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.USER_PROFILE_BLOCKED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void validateUserProfile_ShouldUserNotFound() {
        when(userService.isUserExists(TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> tweetValidationHelper.validateUserProfile(TestConstants.USER_ID));
        assertEquals(String.format(ErrorMessage.USER_ID_NOT_FOUND, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void checkTweetTextLength() {
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> tweetValidationHelper.checkTweetTextLength(""));
        Assertions.assertEquals(ErrorMessage.INCORRECT_TWEET_TEXT_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
