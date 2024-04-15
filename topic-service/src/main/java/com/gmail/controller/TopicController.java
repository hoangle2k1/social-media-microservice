package com.gmail.controller;

import com.gmail.constants.PathConstants;
import com.gmail.dto.request.SuggestedTopicsRequest;
import com.gmail.dto.request.TopicsCategoriesRequest;
import com.gmail.dto.response.TopicResponse;
import com.gmail.dto.response.TopicsByCategoriesResponse;
import com.gmail.mapper.TopicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_TOPICS)
public class TopicController {

    private final TopicMapper topicMapper;

    @PostMapping(PathConstants.SUGGESTED)
    public ResponseEntity<List<TopicResponse>> getTopicsByIds(@RequestBody SuggestedTopicsRequest request) {
        return ResponseEntity.ok(topicMapper.getTopicsByIds(request.getTopicsIds()));
    }

    @PostMapping(PathConstants.CATEGORY)
    public ResponseEntity<List<TopicsByCategoriesResponse>> getTopicsByCategories(@RequestBody TopicsCategoriesRequest request) {
        return ResponseEntity.ok(topicMapper.getTopicsByCategories(request.getCategories()));
    }

    @GetMapping(PathConstants.FOLLOWED)
    public ResponseEntity<List<TopicResponse>> getFollowedTopics() {
        return ResponseEntity.ok(topicMapper.getFollowedTopics());
    }

    @GetMapping(PathConstants.FOLLOWED_USER_ID)
    public ResponseEntity<List<TopicResponse>> getFollowedTopicsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(topicMapper.getFollowedTopicsByUserId(userId));
    }

    @GetMapping(PathConstants.NOT_INTERESTED)
    public ResponseEntity<List<TopicResponse>> getNotInterestedTopics() {
        return ResponseEntity.ok(topicMapper.getNotInterestedTopics());
    }

    @GetMapping(PathConstants.NOT_INTERESTED_TOPIC_ID)
    public ResponseEntity<Boolean> processNotInterestedTopic(@PathVariable("topicId") Long topicId) {
        return ResponseEntity.ok(topicMapper.processNotInterestedTopic(topicId));
    }

    @GetMapping(PathConstants.FOLLOW_TOPIC_ID)
    public ResponseEntity<Boolean> processFollowTopic(@PathVariable("topicId") Long topicId) {
        return ResponseEntity.ok(topicMapper.processFollowTopic(topicId));
    }
}
