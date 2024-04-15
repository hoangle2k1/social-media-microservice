package com.gmail.service;

import com.gmail.constants.ErrorMessage;
import com.gmail.exception.ApiRequestException;
import com.gmail.model.Bookmark;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.repository.BookmarkRepository;
import com.gmail.repository.TweetRepository;
import com.gmail.repository.projection.BookmarkProjection;
import com.gmail.service.impl.BookmarkServiceImpl;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.gmail.TweetServiceTestHelper.createMockBookmarkProjectionList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookmarkServiceImplTest extends AbstractAuthTest {

    @Autowired
    private BookmarkServiceImpl bookmarkService;

    @MockBean
    private BookmarkRepository bookmarkRepository;

    @MockBean
    private TweetRepository tweetRepository;

    @MockBean
    private UserService userService;

    private static Tweet tweet;
    private static User authUser;

    @Before
    public void setUp() {
        super.setUp();
        authUser = new User();
        authUser.setId(TestConstants.USER_ID);
        tweet = new Tweet();
        tweet.setDeleted(false);
        tweet.setAuthor(authUser);
    }

    @Test
    public void getUserBookmarks() {
        Page<BookmarkProjection> bookmark = new PageImpl<>(createMockBookmarkProjectionList(), pageable, 20);
        when(bookmarkRepository.getUserBookmarks(authUser, pageable)).thenReturn(bookmark);
        assertEquals(bookmark, bookmarkService.getUserBookmarks(pageable));
        verify(bookmarkRepository, times(1)).getUserBookmarks(authUser, pageable);
    }

    @Test
    public void processUserBookmarks_ShouldDeleteBookmark() {
        Bookmark bookmark = new Bookmark();
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(bookmarkRepository.getUserBookmark(authUser, tweet)).thenReturn(bookmark);
        assertFalse(bookmarkService.processUserBookmarks(TestConstants.TWEET_ID));
        verify(bookmarkRepository, times(1)).getUserBookmark(authUser, tweet);
        verify(bookmarkRepository, times(1)).delete(bookmark);
    }

    @Test
    public void processUserBookmarks_ShouldCreateBookmark() {
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(bookmarkRepository.getUserBookmark(authUser, tweet)).thenReturn(null);
        assertTrue(bookmarkService.processUserBookmarks(TestConstants.TWEET_ID));
        verify(bookmarkRepository, times(1)).getUserBookmark(authUser, tweet);
        verify(bookmarkRepository, times(1)).save(any());
    }

    @Test
    public void processUserBookmarks_ShouldTweetNotFound() {
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> bookmarkService.processUserBookmarks(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.TWEET_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void processUserBookmarks_ShouldTweetDeleted() {
        tweet.setDeleted(true);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> bookmarkService.processUserBookmarks(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.TWEET_DELETED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void processUserBookmarks_ShouldUserNotFound() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> bookmarkService.processUserBookmarks(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void processUserBookmarks_ShouldUserProfileBlocked() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.findById(TestConstants.TWEET_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(false);
        when(userService.isMyProfileBlockedByUser(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> bookmarkService.processUserBookmarks(TestConstants.TWEET_ID));
        Assertions.assertEquals(ErrorMessage.USER_PROFILE_BLOCKED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void getIsTweetBookmarked() {
        when(bookmarkRepository.isUserBookmarkedTweet(TestConstants.USER_ID, TestConstants.TWEET_ID)).thenReturn(true);
        assertTrue(bookmarkService.getIsTweetBookmarked(TestConstants.TWEET_ID));
        verify(bookmarkRepository, times(1)).isUserBookmarkedTweet(TestConstants.USER_ID, TestConstants.TWEET_ID);
    }
}
