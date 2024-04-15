package com.gmail.service.impl;

import com.gmail.dto.request.IdsRequest;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.exception.ApiRequestException;
import com.gmail.feign.TweetClient;
import com.gmail.model.Tag;
import com.gmail.repository.TagRepository;
import com.gmail.repository.TweetTagRepository;
import com.gmail.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gmail.constants.ErrorMessage.TAG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TweetTagRepository tweetTagRepository;
    private final TweetClient tweetClient;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findTop5ByOrderByTweetsQuantityDesc();
    }

    @Override
    public Page<Tag> getTrends(Pageable pageable) {
        return tagRepository.findByOrderByTweetsQuantityDesc(pageable);
    }

    @Override
    public List<TweetResponse> getTweetsByTag(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new ApiRequestException(TAG_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<Long> tweetIds = tweetTagRepository.getTweetIdsByTagId(tag.getId());
        return tweetClient.getTweetsByIds(new IdsRequest(tweetIds));
    }
}
