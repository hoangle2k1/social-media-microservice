package com.gmail.service;

import com.gmail.repository.projection.BookmarkProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {

    Page<BookmarkProjection> getUserBookmarks(Pageable pageable);

    Boolean processUserBookmarks(Long tweetId);

    Boolean getIsTweetBookmarked(Long tweetId);
}
