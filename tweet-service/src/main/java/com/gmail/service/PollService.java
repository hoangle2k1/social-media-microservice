package com.gmail.service;

import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.model.Tweet;
import com.gmail.repository.projection.TweetProjection;

import java.util.List;

public interface PollService {

    TweetResponse createPoll(Long pollDateTime, List<String> choices, Tweet tweet);

    TweetProjection voteInPoll(Long tweetId, Long pollId, Long pollChoiceId);
}
