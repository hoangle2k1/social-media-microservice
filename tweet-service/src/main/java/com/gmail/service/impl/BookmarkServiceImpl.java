package com.gmail.service.impl;

import com.gmail.model.Bookmark;
import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.repository.BookmarkRepository;
import com.gmail.repository.projection.BookmarkProjection;
import com.gmail.service.BookmarkService;
import com.gmail.service.UserService;
import com.gmail.service.util.TweetValidationHelper;
import com.gmail.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final TweetValidationHelper tweetValidationHelper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkProjection> getUserBookmarks(Pageable pageable) {
        User authUser = userService.getAuthUser();
        return bookmarkRepository.getUserBookmarks(authUser, pageable);
    }

    @Override
    @Transactional
    public Boolean processUserBookmarks(Long tweetId) {
        Tweet tweet = tweetValidationHelper.checkValidTweet(tweetId);
        User authUser = userService.getAuthUser();
        Bookmark bookmark = bookmarkRepository.getUserBookmark(authUser, tweet);

        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
            return false;
        } else {
            Bookmark newBookmark = new Bookmark(authUser, tweet);
            bookmarkRepository.save(newBookmark);
            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean getIsTweetBookmarked(Long tweetId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return bookmarkRepository.isUserBookmarkedTweet(authUserId, tweetId);
    }
}
