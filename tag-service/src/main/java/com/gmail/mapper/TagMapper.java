package com.gmail.mapper;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.TagResponse;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TagMapper {

    private final BasicMapper basicMapper;
    private final TagService tagService;

    public List<TagResponse> getTags() {
        return basicMapper.convertToResponseList(tagService.getTags(), TagResponse.class);
    }

    public HeaderResponse<TagResponse> getTrends(Pageable pageable) {
        return basicMapper.getHeaderResponse(tagService.getTrends(pageable), TagResponse.class);
    }

    public List<TweetResponse> getTweetsByTag(String tagName) {
        return tagService.getTweetsByTag(tagName);
    }
}
