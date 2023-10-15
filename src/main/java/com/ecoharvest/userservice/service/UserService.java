package com.ecoharvest.userservice.service;

import com.ecoharvest.userservice.dto.*;
import com.ecoharvest.userservice.exception.UserException;
import com.ecoharvest.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(RegisterReqDTO user);

    Optional<User> findByUsername(String username);

    List<User> retrieveUserList();

    User retrieveUserById(Long id) throws UserException;

    User updateUserDetails(Long id, String name, String email, String contactNo) throws UserException;

    User changePassword(Long id, String password) throws UserException;

    List<User> deleteUser(Long id) throws UserException;

    User signInAndReturnJWT(LoginReqDTO loginReqDTO);
}
