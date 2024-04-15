package com.gmail.repository.projection;

import com.gmail.model.User;
import org.springframework.beans.factory.annotation.Value;

public interface ListUserProjection {
    Long getId();
    String getListName();
    String getDescription();
    String getAltWallpaper();
    String getWallpaper();
    User getListOwner();
    boolean getIsPrivate();

    @Value("#{@listsServiceHelper.isListPinned(target.id)}")
    boolean getIsListPinned();
}
