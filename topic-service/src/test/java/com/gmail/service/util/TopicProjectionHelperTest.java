package com.gmail.service.util;

import com.gmail.repository.TopicRepository;
import com.gmail.util.TestUtil;
import com.gmail.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TopicProjectionHelperTest {

    @Autowired
    private TopicProjectionHelper topicProjectionHelper;

    @MockBean
    private TopicRepository topicRepository;

    @Before
    public void setUp() {
        TestUtil.mockAuthenticatedUserId();
    }

    @Test
    public void isTopicFollowed() {
        when(topicRepository.isTopicFollowed(TestConstants.USER_ID, 3L)).thenReturn(true);
        assertTrue(topicProjectionHelper.isTopicFollowed(3L));
        verify(topicRepository, times(1)).isTopicFollowed(TestConstants.USER_ID, 3L);
    }

    @Test
    public void isTopicNotInterested() {
        when(topicRepository.isTopicNotInterested(TestConstants.USER_ID, 3L)).thenReturn(true);
        assertTrue(topicProjectionHelper.isTopicNotInterested(3L));
        verify(topicRepository, times(1)).isTopicNotInterested(TestConstants.USER_ID, 3L);
    }
}
