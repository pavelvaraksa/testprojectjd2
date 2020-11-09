package com.noirix.controller.request;

import lombok.Data;

@Data
public class CarCreateRequest {
    private String model;

    private String color;

    private Long userId;
}
