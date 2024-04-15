package com.gmail.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchTermsRequest {
    private List<Long> users;
}
