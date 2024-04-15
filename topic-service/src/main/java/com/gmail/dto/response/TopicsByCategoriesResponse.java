package com.gmail.dto.response;

import com.gmail.enums.TopicCategory;
import com.gmail.repository.projection.TopicProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicsByCategoriesResponse {
    private TopicCategory topicCategory;
    private List<TopicProjection> topicsByCategories;
}
