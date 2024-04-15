package com.gmail.service.cron;

import com.gmail.constants.WebsocketConstants;
import com.gmail.dto.response.tweet.TweetResponse;
import com.gmail.feign.WebSocketClient;
import com.gmail.model.Tweet;
import com.gmail.producer.UpdateTweetCountProducer;
import com.gmail.repository.TweetRepository;
import com.gmail.service.util.TweetServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CronService {

    private final WebSocketClient webSocketClient;
    private final TweetRepository tweetRepository;
    private final TweetServiceHelper tweetServiceHelper;
    private final UpdateTweetCountProducer updateTweetCountProducer;

    @Scheduled(initialDelay = 30000, fixedDelay = 30000)
    public void sendTweetBySchedule() {
        List<Tweet> tweets = tweetRepository.findAllByScheduledDate(LocalDateTime.now());
        tweets.forEach((tweet) -> {
            if (tweet.getText().contains("youtube.com") || !tweet.getImages().isEmpty()) {
                updateTweetCountProducer.sendUpdateMediaTweetCountEvent(tweet.getAuthor().getId(), true);
            } else {
                updateTweetCountProducer.sendUpdateTweetCountEvent(tweet.getAuthor().getId(), true);
            }
            tweet.setScheduledDate(null);
            tweet.setDateTime(LocalDateTime.now());
            TweetResponse tweetResponse = tweetServiceHelper.processTweetResponse(tweet);
            webSocketClient.send(WebsocketConstants.TOPIC_FEED_SCHEDULE, tweetResponse);
        });
    }
}
