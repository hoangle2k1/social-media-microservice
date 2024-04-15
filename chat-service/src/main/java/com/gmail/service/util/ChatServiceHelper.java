package com.gmail.service.util;

import com.gmail.dto.response.chat.ChatTweetResponse;
import com.gmail.exception.ApiRequestException;
import com.gmail.feign.TweetClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.gmail.constants.ErrorMessage.INCORRECT_CHAT_MESSAGE_LENGTH;
import static com.gmail.constants.ErrorMessage.TWEET_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ChatServiceHelper {

    private final TweetClient tweetClient;

    public void checkChatMessageLength(String text) {
        if (text.length() == 0) {
            throw new ApiRequestException(INCORRECT_CHAT_MESSAGE_LENGTH, HttpStatus.BAD_REQUEST);
        }
    }

    public ChatTweetResponse getChatTweet(Long tweetId) {
        return tweetClient.getChatTweet(tweetId);
    }

    public void isTweetExists(Long tweetId) {
        if (!tweetClient.isTweetExists(tweetId)) {
            throw new ApiRequestException(TWEET_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
