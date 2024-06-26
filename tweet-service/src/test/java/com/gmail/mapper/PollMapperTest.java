package com.gmail.mapper;

import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.TweetServiceTestHelper;
import com.gmail.dto.request.TweetRequest;
import com.gmail.dto.request.VoteRequest;
import com.gmail.model.Tweet;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.service.PollService;
import com.gmail.util.AbstractAuthTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PollMapperTest extends AbstractAuthTest {

    @Autowired
    private PollMapper pollMapper;

    @MockBean
    private BasicMapper basicMapper;

    @MockBean
    private PollService pollService;

    @Test
    public void createPoll() {
        TweetRequest tweetRequest = new TweetRequest();
        Tweet tweet = new Tweet();
        TweetResponse tweetResponse = new TweetResponse();
        when(basicMapper.convertToResponse(tweetRequest, Tweet.class)).thenReturn(tweet);
        when(pollService.createPoll(tweetRequest.getPollDateTime(), tweetRequest.getChoices(), tweet)).thenReturn(tweetResponse);
        assertEquals(tweetResponse, pollMapper.createPoll(tweetRequest));
        verify(basicMapper, times(1)).convertToResponse(tweetRequest, Tweet.class);
        verify(pollService, times(1)).createPoll(tweetRequest.getPollDateTime(), tweetRequest.getChoices(), tweet);
    }

    @Test
    public void voteInPoll() {
        TweetResponse tweetResponse = new TweetResponse();
        VoteRequest voteRequest = new VoteRequest();
        TweetProjection tweetProjection = TweetServiceTestHelper.createTweetProjection(false, TweetProjection.class);
        when(pollService.voteInPoll(voteRequest.getTweetId(), voteRequest.getPollId(), voteRequest.getPollChoiceId())).thenReturn(tweetProjection);
        when(basicMapper.convertToResponse(tweetProjection, TweetResponse.class)).thenReturn(tweetResponse);
        assertEquals(tweetResponse, pollMapper.voteInPoll(voteRequest));
        verify(pollService, times(1)).voteInPoll(voteRequest.getTweetId(), voteRequest.getPollId(), voteRequest.getPollChoiceId());
        verify(basicMapper, times(1)).convertToResponse(tweetProjection, TweetResponse.class);
    }
}
