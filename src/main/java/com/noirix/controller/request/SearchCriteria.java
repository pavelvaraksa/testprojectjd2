package com.noirix.controller.request;

import lombok.Data;

@Data
public class SearchCriteria {
    private String query;

    private Long limit;
}
