package com.test.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommonResponse<T> {
    private Integer code;
    private String message;
    private T data;
}