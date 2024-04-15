package com.gmail.service;

import com.gmail.model.Tweet;
import com.gmail.model.User;
import com.gmail.repository.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    User getAuthUser();

    Optional<User> getUserById(Long userId);

    Long getUserIdByUsername(String username);

    Boolean isUserExists(Long userId);

    boolean isUserHavePrivateProfile(Long userId);

    boolean isMyProfileBlockedByUser(Long userId);

    Page<UserProjection> getLikedUsersByTweet(Tweet tweet, Pageable pageable);

    Page<UserProjection> getRetweetedUsersByTweet(Tweet tweet, Pageable pageable);

    Page<UserProjection> getTaggedImageUsers(Tweet tweet, Pageable pageable);
}
