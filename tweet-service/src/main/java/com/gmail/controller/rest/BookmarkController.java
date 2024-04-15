package com.gmail.controller.rest;

import com.gmail.constants.PathConstants;
import com.gmail.dto.HeaderResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.mapper.BookmarkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_TWEETS)
public class BookmarkController {

    private final BookmarkMapper bookmarkMapper;

    @GetMapping(PathConstants.USER_BOOKMARKS)
    public ResponseEntity<List<TweetResponse>> getUserBookmarks(@PageableDefault(size = 10) Pageable pageable) {
        HeaderResponse<TweetResponse> response = bookmarkMapper.getUserBookmarks(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.USER_BOOKMARKS_TWEET_ID)
    public ResponseEntity<Boolean> processUserBookmarks(@PathVariable("tweetId") Long tweetId) {
        return ResponseEntity.ok(bookmarkMapper.processUserBookmarks(tweetId));
    }

    @GetMapping(PathConstants.TWEET_ID_BOOKMARKED)
    public ResponseEntity<Boolean> getIsTweetBookmarked(@PathVariable("tweetId") Long tweetId) {
        return ResponseEntity.ok(bookmarkMapper.getIsTweetBookmarked(tweetId));
    }
}
