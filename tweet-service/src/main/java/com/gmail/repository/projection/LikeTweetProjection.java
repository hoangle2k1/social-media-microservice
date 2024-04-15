package com.gmail.repository.projection;

import java.time.LocalDateTime;

public interface LikeTweetProjection {
    LocalDateTime getLikeTweetDate();
    TweetProjection getTweet();
}
