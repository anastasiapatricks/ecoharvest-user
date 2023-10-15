package com.ecoharvest.userservice.dto;

import com.ecoharvest.userservice.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterResDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String contactNo;
    private LocalDateTime createdDateTime;
    private Role role;
}
