package com.ecoharvest.userservice.dto;

import com.ecoharvest.userservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterReqDTO implements Serializable {
    private String username;
    private String password;
    private String name;
    private String email;
    private String contactNo;
    private Role role;
}
