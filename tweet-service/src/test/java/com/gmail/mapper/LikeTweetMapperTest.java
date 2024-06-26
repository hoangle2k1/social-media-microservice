package com.gmail.mapper;

import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.dto.response.user.UserResponse;
import com.gmail.TweetServiceTestHelper;
import com.gmail.dto.HeaderResponse;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.LikeTweetService;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LikeTweetMapperTest extends AbstractAuthTest {

    @Autowired
    private LikeTweetMapper likeTweetMapper;

    @MockBean
    private BasicMapper basicMapper;

    @MockBean
    private LikeTweetService likeTweetService;

    @Test
    public void getLikedUsersByTweetId() {
        HeaderResponse<UserResponse> headerResponse = new HeaderResponse<>(
                List.of(new UserResponse(), new UserResponse()), new HttpHeaders());
        Page<UserProjection> userProjections = TweetServiceTestHelper.createUserProjections();
        when(likeTweetService.getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable)).thenReturn(userProjections);
        when(basicMapper.getHeaderResponse(userProjections, UserResponse.class)).thenReturn(headerResponse);
        assertNotNull(likeTweetMapper.getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable));
        verify(likeTweetService, times(1)).getLikedUsersByTweetId(TestConstants.TWEET_ID, pageable);
    }

    @Test
    public void likeTweet() {
        NotificationResponse notificationResponse = new NotificationResponse();
        when(likeTweetService.likeTweet(TestConstants.TWEET_ID)).thenReturn(notificationResponse);
        assertEquals(notificationResponse, likeTweetMapper.likeTweet(TestConstants.TWEET_ID));
        verify(likeTweetService, times(1)).likeTweet(TestConstants.TWEET_ID);
    }
}
