package com.gmail.dto.request;

import com.gmail.enums.TopicCategory;
import lombok.Data;

import java.util.List;

@Data
public class TopicsCategoriesRequest {
    private List<TopicCategory> categories;
}
