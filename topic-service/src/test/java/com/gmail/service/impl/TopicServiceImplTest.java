package com.gmail.service.impl;

import com.gmail.constants.ErrorMessage;
import com.gmail.enums.TopicCategory;
import com.gmail.exception.ApiRequestException;
import com.gmail.TopicTestHelper;
import com.gmail.dto.response.TopicsByCategoriesResponse;
import com.gmail.model.Topic;
import com.gmail.model.User;
import com.gmail.repository.TopicRepository;
import com.gmail.repository.UserRepository;
import com.gmail.repository.projection.FollowedTopicProjection;
import com.gmail.repository.projection.NotInterestedTopicProjection;
import com.gmail.repository.projection.TopicProjection;
import com.gmail.service.TopicService;
import com.gmail.util.TestUtil;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TopicServiceImplTest {

    @Autowired
    private TopicService topicService;

    @MockBean
    private TopicRepository topicRepository;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        TestUtil.mockAuthenticatedUserId();
    }

    @Test
    public void getTags() {
        List<Long> longs = Arrays.asList(1L, 2L);
        when(topicRepository.getTopicsByIds(longs)).thenReturn(TopicTestHelper.getMockTopicProjectionList());
        List<TopicProjection> topics = topicService.getTopicsByIds(longs);
        assertEquals(2, topics.size());
        verify(topicRepository, times(1)).getTopicsByIds(longs);
    }

    @Test
    public void getTopicsByCategories() {
        List<TopicProjection> topicProjections = TopicTestHelper.getMockTopicProjectionList();
        when(topicRepository.getTopicsByCategory(TopicCategory.TRAVEL)).thenReturn(List.of(topicProjections.get(0)));
        when(topicRepository.getTopicsByCategory(TopicCategory.FOOD)).thenReturn(List.of(topicProjections.get(1)));
        List<TopicsByCategoriesResponse> topics = topicService.getTopicsByCategories(
                Arrays.asList(TopicCategory.TRAVEL, TopicCategory.FOOD));
        assertEquals(2, topics.size());
        assertEquals(TopicCategory.TRAVEL, topics.get(0).getTopicCategory());
        assertEquals(TopicCategory.FOOD, topics.get(1).getTopicCategory());
        verify(topicRepository, times(2)).getTopicsByCategory(any());
    }

    @Test
    public void getFollowedTopics() {
        when(topicRepository.getTopicsByTopicFollowerId(TestConstants.USER_ID, FollowedTopicProjection.class))
                .thenReturn(TopicTestHelper.getMockFollowedTopicProjectionList());
        List<FollowedTopicProjection> topics = topicService.getFollowedTopics();
        assertEquals(2, topics.size());
    }

    @Test
    public void getFollowedTopicsByUserId() {
        when(userRepository.isUserExists(TestConstants.USER_ID)).thenReturn(true);
        when(topicRepository.getTopicsByTopicFollowerId(TestConstants.USER_ID, TopicProjection.class))
                .thenReturn(TopicTestHelper.getMockTopicProjectionList());
        List<TopicProjection> topics = topicService.getFollowedTopicsByUserId(TestConstants.USER_ID);
        assertEquals(2, topics.size());
        verify(topicRepository, times(1)).getTopicsByTopicFollowerId(TestConstants.USER_ID, TopicProjection.class);
        verify(userRepository, times(1)).isUserExists(TestConstants.USER_ID);
    }

    @Test
    public void getFollowedTopicsByUserId_shouldUserIdNotFound() {
        when(userRepository.isUserExists(TestConstants.USER_ID)).thenReturn(false);
        validateProfileTest(TestConstants.USER_ID, String.format(ErrorMessage.USER_ID_NOT_FOUND, TestConstants.USER_ID), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getFollowedTopicsByUserId_shouldUserProfileBlocked() {
        when(userRepository.isUserExists(1L)).thenReturn(true);
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(true);
        validateProfileTest(1L, ErrorMessage.USER_PROFILE_BLOCKED, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getFollowedTopicsByUserId_shouldUserHavePrivateProfile() {
        when(userRepository.isUserExists(1L)).thenReturn(true);
        when(userRepository.isUserBlocked(1L, TestConstants.USER_ID)).thenReturn(false);
        when(userRepository.isUserHavePrivateProfile(1L, TestConstants.USER_ID)).thenReturn(false);
        validateProfileTest(1L, ErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Test
    public void getNotInterestedTopics() {
        when(topicRepository.getTopicsByNotInterestedUserId(TestConstants.USER_ID))
                .thenReturn(TopicTestHelper.getMockNotInterestedTopicProjectionList());
        List<NotInterestedTopicProjection> topics = topicService.getNotInterestedTopics();
        assertEquals(2, topics.size());
        verify(topicRepository, times(1)).getTopicsByNotInterestedUserId(TestConstants.USER_ID);
    }

    @Test
    public void processNotInterestedTopic_removeTopic() {
        User authUser = TopicTestHelper.mockAuthUser();
        Topic topic = mockTopic();
        topic.setTopicNotInterested(new HashSet<>(Set.of(authUser)));
        when(topicRepository.findById(TestConstants.TOPIC_ID)).thenReturn(Optional.of(topic));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertFalse(topicService.processNotInterestedTopic(TestConstants.TOPIC_ID));
        verify(topicRepository, times(1)).findById(TestConstants.TOPIC_ID);
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    public void processNotInterestedTopic_addTopic() {
        User authUser = TopicTestHelper.mockAuthUser();
        Topic topic = mockTopic();
        topic.setTopicNotInterested(new HashSet<>());
        when(topicRepository.findById(TestConstants.TOPIC_ID)).thenReturn(Optional.of(topic));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertTrue(topicService.processNotInterestedTopic(TestConstants.TOPIC_ID));
        verify(topicRepository, times(1)).findById(TestConstants.TOPIC_ID);
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    public void processNotInterestedTopic_topicNotFound() {
        when(topicRepository.isTopicExist(3L)).thenReturn(false);
        try {
            topicService.processNotInterestedTopic(3L);
        } catch (ApiRequestException exception) {
            Assertions.assertEquals(ErrorMessage.TOPIC_NOT_FOUND, exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }

    @Test
    public void processFollowTopic_unfollowTopic() {
        User authUser = TopicTestHelper.mockAuthUser();
        Topic topic = mockTopic();
        topic.setTopicFollowers(new HashSet<>(Set.of(authUser)));
        when(topicRepository.findById(TestConstants.TOPIC_ID)).thenReturn(Optional.of(topic));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertFalse(topicService.processFollowTopic(TestConstants.TOPIC_ID));
        verify(topicRepository, times(1)).findById(TestConstants.TOPIC_ID);
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    public void processFollowTopic_followTopic() {
        User authUser = TopicTestHelper.mockAuthUser();
        Topic topic = mockTopic();
        topic.setTopicFollowers(new HashSet<>());
        when(topicRepository.findById(TestConstants.TOPIC_ID)).thenReturn(Optional.of(topic));
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));
        assertTrue(topicService.processFollowTopic(TestConstants.TOPIC_ID));
        verify(topicRepository, times(1)).findById(TestConstants.TOPIC_ID);
        verify(userRepository, times(1)).findById(TestConstants.USER_ID);
    }

    @Test
    public void processFollowTopic_topicNotFound() {
        when(topicRepository.isTopicExist(3L)).thenReturn(false);
        try {
            topicService.processFollowTopic(3L);
        } catch (ApiRequestException exception) {
            Assertions.assertEquals(ErrorMessage.TOPIC_NOT_FOUND, exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }

    private void validateProfileTest(Long userId, String testMessage, HttpStatus status) {
        try {
            topicService.getFollowedTopicsByUserId(userId);
        } catch (ApiRequestException exception) {
            assertEquals(testMessage, exception.getMessage());
            assertEquals(status, exception.getStatus());
        }
    }

    private Topic mockTopic() {
        Topic topic = new Topic();
        topic.setId(TestConstants.TOPIC_ID);
        topic.setTopicName(TestConstants.TOPIC_NAME);
        topic.setTopicCategory(TopicCategory.GAMING);
        return topic;
    }
}
