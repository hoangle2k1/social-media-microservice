package com.gmail.mapper;

import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.dto.request.TweetRequest;
import com.gmail.dto.request.VoteRequest;
import com.gmail.model.Tweet;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PollMapper {

    private final BasicMapper basicMapper;
    private final PollService pollService;

    public TweetResponse createPoll(TweetRequest tweetRequest) {
        Tweet tweet = basicMapper.convertToResponse(tweetRequest, Tweet.class);
        return pollService.createPoll(tweetRequest.getPollDateTime(), tweetRequest.getChoices(), tweet);
    }

    public TweetResponse voteInPoll(VoteRequest voteRequest) {
        TweetProjection tweet = pollService.voteInPoll(voteRequest.getTweetId(), voteRequest.getPollId(),
                voteRequest.getPollChoiceId());
        return basicMapper.convertToResponse(tweet, TweetResponse.class);
    }
}
