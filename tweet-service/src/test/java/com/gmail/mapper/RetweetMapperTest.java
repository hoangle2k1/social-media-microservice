package com.gmail.mapper;

import com.gmail.dto.HeaderResponse;
import com.gmail.dto.response.notification.NotificationResponse;
import com.gmail.dto.response.user.UserResponse;
import com.gmail.TweetServiceTestHelper;
import com.gmail.dto.response.TweetUserResponse;
import com.gmail.repository.projection.TweetUserProjection;
import com.gmail.repository.projection.UserProjection;
import com.gmail.service.RetweetService;
import com.gmail.util.AbstractAuthTest;
import com.gmail.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RetweetMapperTest extends AbstractAuthTest {

    @Autowired
    private RetweetMapper retweetMapper;

    @MockBean
    private BasicMapper basicMapper;

    @MockBean
    private RetweetService retweetService;

    @Test
    public void getUserRetweetsAndReplies() {
        List<TweetUserProjection> tweetUserProjections = TweetServiceTestHelper.createMockTweetUserProjectionList();
        Page<TweetUserProjection> pageableTweetUserProjections = new PageImpl<>(tweetUserProjections, pageable, 20);
        HeaderResponse<TweetUserResponse> headerResponse = new HeaderResponse<>(
                List.of(new TweetUserResponse(), new TweetUserResponse()), new HttpHeaders());
        when(retweetService.getUserRetweetsAndReplies(TestConstants.USER_ID, pageable)).thenReturn(pageableTweetUserProjections);
        when(basicMapper.getHeaderResponse(pageableTweetUserProjections, TweetUserResponse.class)).thenReturn(headerResponse);
        assertEquals(headerResponse, retweetMapper.getUserRetweetsAndReplies(TestConstants.USER_ID, pageable));
        verify(retweetService, times(1)).getUserRetweetsAndReplies(TestConstants.USER_ID, pageable);
        verify(basicMapper, times(1)).getHeaderResponse(pageableTweetUserProjections, TweetUserResponse.class);
    }

    @Test
    public void getRetweetedUsersByTweetId() {
        HeaderResponse<UserResponse> headerResponse = new HeaderResponse<>(
                List.of(new UserResponse(), new UserResponse()), new HttpHeaders());
        Page<UserProjection> userProjections = TweetServiceTestHelper.createUserProjections();
        when(basicMapper.getHeaderResponse(userProjections, UserResponse.class)).thenReturn(headerResponse);
        when(retweetService.getRetweetedUsersByTweetId(TestConstants.TWEET_ID, pageable)).thenReturn(userProjections);
        assertEquals(headerResponse, retweetMapper.getRetweetedUsersByTweetId(TestConstants.TWEET_ID, pageable));
        verify(retweetService, times(1)).getRetweetedUsersByTweetId(TestConstants.TWEET_ID, pageable);
    }

    @Test
    public void retweet() {
        NotificationResponse notificationResponse = new NotificationResponse();
        when(retweetService.retweet(TestConstants.TWEET_ID)).thenReturn(notificationResponse);
        assertEquals(notificationResponse, retweetMapper.retweet(TestConstants.TWEET_ID));
        verify(retweetService, times(1)).retweet(TestConstants.TWEET_ID);
    }
}
