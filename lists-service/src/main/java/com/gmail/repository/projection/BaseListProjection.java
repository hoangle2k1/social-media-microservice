package com.gmail.repository.projection;

import com.gmail.model.User;
import org.springframework.beans.factory.annotation.Value;

public interface BaseListProjection {
    Long getId();
    String getListName();
    String getDescription();
    String getAltWallpaper();
    String getWallpaper();
    User getListOwner();
    boolean getIsPrivate();

    @Value("#{@listsRepository.getMembersSize(target.id)}")
    Long getMembersSize();

    @Value("#{@listsRepository.getFollowersSize(target.id)}")
    Long getFollowersSize();

    @Value("#{@listsServiceHelper.isMyProfileFollowList(target.id)}")
    boolean getIsFollower();
}
