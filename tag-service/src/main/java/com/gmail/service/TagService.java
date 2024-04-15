package com.gmail.service;

import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    List<Tag> getTags();

    Page<Tag> getTrends(Pageable pageable);

    List<TweetResponse> getTweetsByTag(String tagName);
}
