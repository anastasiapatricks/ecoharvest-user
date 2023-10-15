package com.ecoharvest.userservice.dto;

import com.ecoharvest.userservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginResDTO implements Serializable {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String contactNo;
    private Role role;
    private String token;
}
