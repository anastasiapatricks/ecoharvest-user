package com.ecoharvest.userservice.controller;

import com.ecoharvest.userservice.dto.*;
import com.ecoharvest.userservice.exception.UserException;
import com.ecoharvest.userservice.model.User;
import com.ecoharvest.userservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ecoharvest.userservice.constants.ApplicationConstants.*;

@RestController
@RequestMapping(USER_API_PATH)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(RETRIEVE_USER_LIST)
    public BaseResponse<List<User>> retrieveUserList() {
        BaseResponse<List<User>> response = new BaseResponse<>();

        try {
            ResponseEntity<List<User>> res = new ResponseEntity<>(userService.retrieveUserList(), HttpStatus.OK);
            response.setResult(SUCCESS_CODE);
            response.setPayload(res.getBody());
            response.setMessage(null);
        } catch (Exception e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(RETRIEVE_USER_LIST_ERR_MSG);
        }

        return response;
    }

    @GetMapping(RETRIEVE_USER)
    public BaseResponse<User> retrieveUserById(@RequestParam Long id) throws UserException {
        BaseResponse<User> response = new BaseResponse<>();

        try {
            ResponseEntity<User> res = new ResponseEntity<>(userService.retrieveUserById(id), HttpStatus.OK);
            response.setResult(SUCCESS_CODE);
            response.setPayload(res.getBody());
            response.setMessage(null);
        } catch (UserException e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(ERR_MSG);
        }

        return response;
    }

    @PutMapping(UPDATE_USER_DETAILS)
    public BaseResponse<User> updateUserDetails(@RequestParam Long id, @RequestParam(required = false) String name, @RequestParam(required = false) String email, @RequestParam(required = false) String contactNo) throws UserException {
        BaseResponse<User> response = new BaseResponse<>();

        try {
            ResponseEntity<User> res = new ResponseEntity<>(userService.updateUserDetails(id, name, email, contactNo), HttpStatus.OK);
            response.setResult(SUCCESS_CODE);
            response.setPayload(res.getBody());
            response.setMessage(null);
        } catch (UserException e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(ERR_MSG);
        }

        return response;
    }

    @PutMapping(CHANGE_PASSWORD)
    public BaseResponse<User> changePassword(@RequestParam Long id, @RequestParam String password) throws UserException {
        BaseResponse<User> response = new BaseResponse<>();

        try {
            ResponseEntity<User> res = new ResponseEntity<>(userService.changePassword(id, password), HttpStatus.OK);
            response.setResult(SUCCESS_CODE);
            response.setPayload(res.getBody());
            response.setMessage(null);
        } catch (UserException e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(ERR_MSG);
        }

        return response;
    }

    @DeleteMapping(DELETE_USER)
    public BaseResponse<List<User>> deleteUser(@RequestParam Long id) throws UserException {
        BaseResponse<List<User>> response = new BaseResponse<>();

        try {
            ResponseEntity<List<User>> res = new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
            response.setResult(SUCCESS_CODE);
            response.setPayload(res.getBody());
            response.setMessage(null);
        } catch (UserException e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setResult(ERROR_CODE);
            response.setPayload(null);
            response.setMessage(ERR_MSG);
        }

        return response;
    }

    @PostMapping(REGISTER)
    public BaseResponse<User> register(@RequestBody RegisterReqDTO user) {
        BaseResponse<User> response = new BaseResponse<>();

        try {
            ResponseEntity<User> res = new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
            response.setResult(SUCCESS_CODE);
            response.setMessage(null);
            response.setPayload(res.getBody());
        } catch (Exception e) {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                response.setResult(ERROR_CODE);
                response.setMessage(REGISTER_USERNAME_ERR_MSG);
                response.setPayload(null);
            } else {
                response.setResult(ERROR_CODE);
                response.setMessage(REGISTER_ERR_MSG);
                response.setPayload(null);
            }
        }

        return response;
    }

    @PostMapping(LOGIN)
    public BaseResponse<User> login(@RequestBody LoginReqDTO loginReqDTO) {
        BaseResponse<User> response = new BaseResponse<>();

        try {
            ResponseEntity<User> res = new ResponseEntity<>(userService.signInAndReturnJWT(loginReqDTO), HttpStatus.OK);
            response.setResult(SUCCESS_CODE);
            response.setMessage(null);
            response.setPayload(res.getBody());
        } catch (Exception e) {
            response.setResult(ERROR_CODE);
            response.setMessage(LOGIN_ERR_MSG);
            response.setPayload(null);
        }

        return response;
    }
}
