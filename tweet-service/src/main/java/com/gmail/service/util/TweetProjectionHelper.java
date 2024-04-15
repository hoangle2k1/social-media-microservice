package com.gmail.service.util;

import com.gmail.dto.response.tweet.TweetListResponse;
import com.gmail.feign.ListsClient;
import com.gmail.repository.BookmarkRepository;
import com.gmail.repository.LikeTweetRepository;
import com.gmail.repository.RetweetRepository;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TweetProjectionHelper {

    private final LikeTweetRepository likeTweetRepository;
    private final RetweetRepository retweetRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ListsClient listsClient;

    public boolean isUserLikedTweet(Long tweetId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return likeTweetRepository.isUserLikedTweet(authUserId, tweetId);
    }

    public boolean isUserRetweetedTweet(Long tweetId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return retweetRepository.isUserRetweetedTweet(authUserId, tweetId);
    }

    public boolean isUserBookmarkedTweet(Long tweetId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return bookmarkRepository.isUserBookmarkedTweet(authUserId, tweetId);
    }

    public TweetListResponse getTweetList(Long listId) {
        TweetListResponse tweetList = listsClient.getTweetList(listId);

        if (tweetList.getId() != null) {
            return tweetList;
        } else {
            return null;
        }
    }
}
