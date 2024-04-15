package com.gmail.service;

import com.gmail.constants.ErrorMessage;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.exception.ApiRequestException;
import com.gmail.TweetServiceTestHelper;
import com.gmail.model.*;
import com.gmail.model.*;
import com.gmail.repository.PollChoiceRepository;
import com.gmail.repository.PollChoiceVotedRepository;
import com.gmail.repository.PollRepository;
import com.gmail.repository.TweetRepository;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.service.impl.PollServiceImpl;
import com.gmail.service.impl.TweetServiceImpl;
import com.gmail.service.util.TweetServiceHelper;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PollServiceImplTest extends AbstractAuthTest {

    @Autowired
    private PollServiceImpl pollService;

    @MockBean
    private PollRepository pollRepository;

    @MockBean
    private PollChoiceRepository pollChoiceRepository;

    @MockBean
    private PollChoiceVotedRepository pollChoiceVotedRepository;

    @MockBean
    private TweetServiceImpl tweetService;

    @MockBean
    private TweetServiceHelper tweetServiceHelper;

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
    public void createPoll() {
        List<String> choices = Arrays.asList(TestConstants.POLL_CHOICE_1, TestConstants.POLL_CHOICE_2);
        List<PollChoice> pollChoices = Arrays.asList(new PollChoice(TestConstants.POLL_CHOICE_1), new PollChoice(TestConstants.POLL_CHOICE_2));
        when(tweetServiceHelper.createTweet(new Tweet())).thenReturn(new TweetResponse());
        assertEquals(new TweetResponse(), pollService.createPoll(123L, choices, new Tweet()));
        verify(pollChoiceRepository, times(2)).save(new PollChoice(TestConstants.POLL_CHOICE_1));
        verify(pollChoiceRepository, times(2)).save(new PollChoice(TestConstants.POLL_CHOICE_2));
        verify(pollRepository, times(1)).save(new Poll(LocalDateTime.now().plusMinutes(123L), new Tweet(), pollChoices));
        verify(tweetServiceHelper, times(1)).createTweet(new Tweet());
    }

    @Test
    public void createPoll_ShouldIncorrectPollChoices() {
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> pollService.createPoll(123L, List.of(TestConstants.POLL_CHOICE_1), new Tweet()));
        Assertions.assertEquals(ErrorMessage.INCORRECT_POLL_CHOICES, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void createPoll_ShouldIncorrectPollChoiceTextLength() {
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> pollService.createPoll(123L, Arrays.asList("", TestConstants.POLL_CHOICE_2), new Tweet()));
        Assertions.assertEquals(ErrorMessage.INCORRECT_CHOICE_TEXT_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void voteInPoll() {
        Poll poll = new Poll();
        poll.setDateTime(LocalDateTime.now().plusMinutes(Integer.MAX_VALUE));
        TweetProjection tweetProjection = TweetServiceTestHelper.createTweetProjection(false, TweetProjection.class);
        when(tweetRepository.getTweetByPollIdAndTweetId(TestConstants.TWEET_ID, TestConstants.POLL_ID)).thenReturn(Optional.of(tweet));
        when(pollRepository.getPollByPollChoiceId(TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID)).thenReturn(Optional.of(poll));
        when(pollChoiceVotedRepository.ifUserVoted(TestConstants.USER_ID, TestConstants.POLL_CHOICE_ID)).thenReturn(false);
        when(tweetService.getTweetById(TestConstants.TWEET_ID)).thenReturn(tweetProjection);
        assertEquals(tweetProjection, pollService.voteInPoll(TestConstants.TWEET_ID, TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID));
        verify(tweetRepository, times(1)).getTweetByPollIdAndTweetId(TestConstants.TWEET_ID, TestConstants.POLL_ID);
        verify(pollRepository, times(1)).getPollByPollChoiceId(TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID);
        verify(pollChoiceVotedRepository, times(1)).ifUserVoted(TestConstants.USER_ID, TestConstants.POLL_CHOICE_ID);
        verify(pollChoiceVotedRepository, times(1)).save(new PollChoiceVoted(TestConstants.USER_ID, TestConstants.POLL_CHOICE_ID));
        verify(tweetService, times(1)).getTweetById(TestConstants.TWEET_ID);
    }

    @Test
    public void voteInPoll_ShouldPollNotFound() {
        when(tweetRepository.getTweetByPollIdAndTweetId(TestConstants.TWEET_ID, TestConstants.POLL_ID)).thenReturn(Optional.empty());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> pollService.voteInPoll(TestConstants.TWEET_ID, TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID));
        Assertions.assertEquals(ErrorMessage.POLL_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void voteInPoll_ShouldUserNotFound() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.getTweetByPollIdAndTweetId(TestConstants.TWEET_ID, TestConstants.POLL_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> pollService.voteInPoll(TestConstants.TWEET_ID, TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID));
        Assertions.assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void voteInPoll_ShouldUserProfileBlocked() {
        User authUser = new User();
        authUser.setId(1L);
        tweet.setAuthor(authUser);
        when(tweetRepository.getTweetByPollIdAndTweetId(TestConstants.TWEET_ID, TestConstants.POLL_ID)).thenReturn(Optional.of(tweet));
        when(userService.isUserHavePrivateProfile(1L)).thenReturn(false);
        when(userService.isMyProfileBlockedByUser(1L)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> pollService.voteInPoll(TestConstants.TWEET_ID, TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID));
        Assertions.assertEquals(ErrorMessage.USER_PROFILE_BLOCKED, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void voteInPoll_ShouldPollIsNotAvailable() {
        Poll poll = new Poll();
        poll.setDateTime(LocalDateTime.now());
        when(tweetRepository.getTweetByPollIdAndTweetId(TestConstants.TWEET_ID, TestConstants.POLL_ID)).thenReturn(Optional.of(tweet));
        when(pollRepository.getPollByPollChoiceId(TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID)).thenReturn(Optional.of(poll));
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> pollService.voteInPoll(TestConstants.TWEET_ID, TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID));
        Assertions.assertEquals(ErrorMessage.POLL_IS_NOT_AVAILABLE, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void voteInPoll_ShouldUserVotedInPoll() {
        Poll poll = new Poll();
        poll.setDateTime(LocalDateTime.now().plusMinutes(Integer.MAX_VALUE));
        when(tweetRepository.getTweetByPollIdAndTweetId(TestConstants.TWEET_ID, TestConstants.POLL_ID)).thenReturn(Optional.of(tweet));
        when(pollRepository.getPollByPollChoiceId(TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID)).thenReturn(Optional.of(poll));
        when(pollChoiceVotedRepository.ifUserVoted(TestConstants.USER_ID, TestConstants.POLL_CHOICE_ID)).thenReturn(true);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> pollService.voteInPoll(TestConstants.TWEET_ID, TestConstants.POLL_ID, TestConstants.POLL_CHOICE_ID));
        Assertions.assertEquals(ErrorMessage.USER_VOTED_IN_POLL, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
