package com.ecoharvest.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseResponse<T> implements Serializable {
    private String result;
    private String message;
    private T payload;
}
