package com.gmail.service.impl;

import com.gmail.constants.ErrorMessage;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.exception.ApiRequestException;
import com.gmail.model.Poll;
import com.gmail.model.PollChoice;
import com.gmail.model.PollChoiceVoted;
import com.gmail.model.Tweet;
import com.gmail.repository.PollChoiceRepository;
import com.gmail.repository.PollChoiceVotedRepository;
import com.gmail.repository.PollRepository;
import com.gmail.repository.TweetRepository;
import com.gmail.repository.projection.TweetProjection;
import com.gmail.service.PollService;
import com.gmail.service.TweetService;
import com.gmail.service.util.TweetValidationHelper;
import com.gmail.util.AuthUtil;
import com.gmail.service.util.TweetServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;
    private final PollChoiceRepository pollChoiceRepository;
    private final PollChoiceVotedRepository pollChoiceVotedRepository;
    private final TweetService tweetService;
    private final TweetServiceHelper tweetServiceHelper;
    private final TweetValidationHelper tweetValidationHelper;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public TweetResponse createPoll(Long pollDateTime, List<String> choices, Tweet tweet) {
        if (choices.size() < 2 || choices.size() > 4) {
            throw new ApiRequestException(ErrorMessage.INCORRECT_POLL_CHOICES, HttpStatus.BAD_REQUEST);
        }
        List<PollChoice> pollChoices = new ArrayList<>();
        choices.forEach(choice -> {
            if (choice.length() == 0 || choice.length() > 25) {
                throw new ApiRequestException(ErrorMessage.INCORRECT_CHOICE_TEXT_LENGTH, HttpStatus.BAD_REQUEST);
            }
            PollChoice pollChoice = new PollChoice(choice);
            pollChoiceRepository.save(pollChoice);
            pollChoices.add(pollChoice);
        });
        Poll poll = new Poll(LocalDateTime.now().plusMinutes(pollDateTime), tweet, pollChoices);
        pollRepository.save(poll);
        tweet.setPoll(poll);
        return tweetServiceHelper.createTweet(tweet);
    }

    @Override
    @Transactional
    public TweetProjection voteInPoll(Long tweetId, Long pollId, Long pollChoiceId) {
        Tweet tweet = tweetRepository.getTweetByPollIdAndTweetId(tweetId, pollId)
                .orElseThrow(() -> new ApiRequestException(ErrorMessage.POLL_NOT_FOUND, HttpStatus.NOT_FOUND));
        tweetValidationHelper.checkIsValidUserProfile(tweet.getAuthor().getId());
        Poll poll = pollRepository.getPollByPollChoiceId(pollId, pollChoiceId)
                .orElseThrow(() -> new ApiRequestException(ErrorMessage.POLL_CHOICE_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (LocalDateTime.now().isAfter(poll.getDateTime())) {
            throw new ApiRequestException(ErrorMessage.POLL_IS_NOT_AVAILABLE, HttpStatus.BAD_REQUEST);
        }
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        if (pollChoiceVotedRepository.ifUserVoted(authUserId, pollChoiceId)) {
            throw new ApiRequestException(ErrorMessage.USER_VOTED_IN_POLL, HttpStatus.BAD_REQUEST);
        }
        pollChoiceVotedRepository.save(new PollChoiceVoted(authUserId, pollChoiceId));
        return tweetService.getTweetById(tweetId);
    }
}
