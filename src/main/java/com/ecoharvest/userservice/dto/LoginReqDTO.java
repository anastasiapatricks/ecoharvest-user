package com.ecoharvest.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginReqDTO implements Serializable {
    private String username;
    private String password;
}
