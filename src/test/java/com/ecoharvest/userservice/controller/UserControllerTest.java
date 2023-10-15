package com.ecoharvest.userservice.controller;

import com.ecoharvest.userservice.dto.BaseResponse;
import com.ecoharvest.userservice.dto.LoginReqDTO;
import com.ecoharvest.userservice.dto.RegisterReqDTO;
import com.ecoharvest.userservice.exception.UserException;
import com.ecoharvest.userservice.model.User;
import com.ecoharvest.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ecoharvest.userservice.constants.TestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOG = LogManager.getLogger(UserControllerTest.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Retrieve User List - Success Flow")
    void retrieveUserListSuccess() throws IOException {
        // given
        User expectedUser_01 = objectMapper.readValue(new File(USER_01), User.class);
        User expectedUser_02 = objectMapper.readValue(new File(USER_02), User.class);

        List<User> userList = new ArrayList<>();
        userList.add(expectedUser_01);
        userList.add(expectedUser_02);

        // when
        when(userService.retrieveUserList()).thenReturn(userList);
        BaseResponse<List<User>> retrieveUserList = userController.retrieveUserList();

        // then
        assertThat(retrieveUserList.getResult()).isEqualTo(SUCCESS_CODE);
        assertThat(retrieveUserList.getPayload().get(0).getId()).isEqualTo(expectedUser_01.getId());
        assertThat(retrieveUserList.getPayload().get(0).getUsername()).isEqualTo(expectedUser_01.getUsername());
        assertThat(retrieveUserList.getPayload().get(0).getName()).isEqualTo(expectedUser_01.getName());
        assertThat(retrieveUserList.getPayload().get(1).getId()).isEqualTo(expectedUser_02.getId());
        assertThat(retrieveUserList.getPayload().get(1).getUsername()).isEqualTo(expectedUser_02.getUsername());
        assertThat(retrieveUserList.getPayload().get(1).getName()).isEqualTo(expectedUser_02.getName());
    }

    @Test
    @DisplayName("Retrieve User List - Fail Exception Flow")
    void retrieveUserListFail() {
        // given
        when(userService.retrieveUserList()).thenThrow(RuntimeException.class);

        // then
        try {
            userController.retrieveUserList();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).retrieveUserList();
    }

    @Test
    @DisplayName("Retrieve User By Id - Success Flow")
    void retrieveUserByIdSuccess() throws IOException {
        // given
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);

        // when
        when(userService.retrieveUserById(ID)).thenReturn(expectedUser);
        BaseResponse<User> retrieveUserById = userController.retrieveUserById(ID);

        // then
        assertThat(retrieveUserById.getResult()).isEqualTo(SUCCESS_CODE);
        assertThat(retrieveUserById.getPayload().getId()).isEqualTo(expectedUser.getId());
        assertThat(retrieveUserById.getPayload().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(retrieveUserById.getPayload().getName()).isEqualTo(expectedUser.getName());
        assertThat(retrieveUserById.getPayload().getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(retrieveUserById.getPayload().getContactNo()).isEqualTo(expectedUser.getContactNo());
    }

    @Test
    @DisplayName("Retrieve User By Id - Fail UserException Flow")
    void retrieveUserByIdFailUserException() {
        // given
        when(userService.retrieveUserById(ID)).thenThrow(UserException.class);

        // then
        try {
            userController.retrieveUserById(ID);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).retrieveUserById(ID);
    }

    @Test
    @DisplayName("Retrieve User By Id - Fail Exception Flow")
    void retrieveUserByIdFailException() {
        // given
        when(userService.retrieveUserById(ID)).thenThrow(RuntimeException.class);

        // then
        try {
            userController.retrieveUserById(ID);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).retrieveUserById(ID);
    }

    @Test
    @DisplayName("Update User Details - Success Flow")
    void updateUserDetailsSuccess() throws IOException {
        // given
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);
        expectedUser.setName(CHANGED_NAME);
        expectedUser.setEmail(CHANGED_EMAIL);
        expectedUser.setContactNo(CHANGED_CONTACT_NO);

        // when
        when(userService.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO)).thenReturn(expectedUser);
        BaseResponse<User> updateUserDetails = userController.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO);

        // then
        assertThat(updateUserDetails.getResult()).isEqualTo(SUCCESS_CODE);
        assertThat(updateUserDetails.getPayload().getId()).isEqualTo(expectedUser.getId());
        assertThat(updateUserDetails.getPayload().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(updateUserDetails.getPayload().getName()).isEqualTo(CHANGED_NAME);
        assertThat(updateUserDetails.getPayload().getEmail()).isEqualTo(CHANGED_EMAIL);
        assertThat(updateUserDetails.getPayload().getContactNo()).isEqualTo(CHANGED_CONTACT_NO);
    }

    @Test
    @DisplayName("Update User Details - Fail UserException Flow")
    void updateUserDetailsFailUserException() {
        // given
        when(userService.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO)).thenThrow(UserException.class);

        // then
        try {
            userController.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO);
    }

    @Test
    @DisplayName("Update User Details - Fail Exception Flow")
    void updateUserDetailsFailException() {
        // given
        when(userService.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO)).thenThrow(RuntimeException.class);

        // then
        try {
            userController.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO);
    }

    @Test
    @DisplayName("Change Password - Success Flow")
    void changePasswordSuccess() throws IOException {
        // given
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);
        String encryptedPassword = passwordEncoder.encode(CHANGED_PWD);
        expectedUser.setPassword(encryptedPassword);

        // when
        when(userService.changePassword(ID, CHANGED_PWD)).thenReturn(expectedUser);
        BaseResponse<User> changePassword = userController.changePassword(ID, CHANGED_PWD);

        // then
        assertThat(changePassword.getResult()).isEqualTo(SUCCESS_CODE);
        assertThat(changePassword.getPayload().getId()).isEqualTo(expectedUser.getId());
        assertThat(changePassword.getPayload().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(changePassword.getPayload().getPassword()).isEqualTo(encryptedPassword);
    }

    @Test
    @DisplayName("Change Password - Fail UserException Flow")
    void changePasswordFailUserException() {
        // given
        when(userService.changePassword(ID, CHANGED_PWD)).thenThrow(UserException.class);

        // then
        try {
            userController.changePassword(ID, CHANGED_PWD);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).changePassword(ID, CHANGED_PWD);
    }

    @Test
    @DisplayName("Update User Details - Fail Exception Flow")
    void changePasswordFailException() {
        // given
        when(userService.changePassword(ID, CHANGED_PWD)).thenThrow(RuntimeException.class);

        // then
        try {
            userController.changePassword(ID, CHANGED_PWD);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).changePassword(ID, CHANGED_PWD);
    }

    @Test
    @DisplayName("Delete User - Success Flow")
    void deleteUserSuccess() throws IOException {
        // given
        User expectedUser_02 = objectMapper.readValue(new File(USER_02), User.class);

        List<User> userList = new ArrayList<>();
        userList.add(expectedUser_02);

        // when
        when(userService.deleteUser(ID)).thenReturn(userList);
        BaseResponse<List<User>> deleteUser = userController.deleteUser(ID);

        // then
        assertThat(deleteUser.getResult()).isEqualTo(SUCCESS_CODE);
        assertThat(deleteUser.getPayload().get(0).getId()).isEqualTo(expectedUser_02.getId());
        assertThat(deleteUser.getPayload().get(0).getUsername()).isEqualTo(expectedUser_02.getUsername());
        assertThat(deleteUser.getPayload().get(0).getName()).isEqualTo(expectedUser_02.getName());
        assertThat(deleteUser.getPayload().get(0).getEmail()).isEqualTo(expectedUser_02.getEmail());
        assertThat(deleteUser.getPayload().get(0).getContactNo()).isEqualTo(expectedUser_02.getContactNo());
    }

    @Test
    @DisplayName("Delete User - Fail UserException Flow")
    void deleteUserFailUserException() {
        // given
        when(userService.deleteUser(ID)).thenThrow(UserException.class);

        // then
        try {
            userController.deleteUser(ID);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).deleteUser(ID);
    }

    @Test
    @DisplayName("Delete User - Fail Exception Flow")
    void deleteUserFailException() {
        // given
        when(userService.deleteUser(ID)).thenThrow(RuntimeException.class);

        // then
        try {
            userController.deleteUser(ID);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).deleteUser(ID);
    }

    @Test
    @DisplayName("Login - Success Flow")
    void loginSuccess() throws IOException {
        // given
        LoginReqDTO loginReqDTO = objectMapper.readValue(new File(LOGIN_REQ_DTO_01), LoginReqDTO.class);
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);
        expectedUser.setToken(TOKEN);

        // when
        when(userService.signInAndReturnJWT(loginReqDTO)).thenReturn(expectedUser);
        BaseResponse<User> loginUser = userController.login(loginReqDTO);

        // then
        assertThat(loginUser.getResult()).isEqualTo(SUCCESS_CODE);
        assertThat(loginUser.getPayload().getId()).isEqualTo(expectedUser.getId());
        assertThat(loginUser.getPayload().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(loginUser.getPayload().getName()).isEqualTo(expectedUser.getName());
        assertThat(loginUser.getPayload().getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(loginUser.getPayload().getContactNo()).isEqualTo(expectedUser.getContactNo());
        assertThat(loginUser.getPayload().getToken()).isEqualTo(TOKEN);
    }

    @Test
    @DisplayName("Login - Fail Exception Flow")
    void loginFailException() throws IOException {
        // given
        LoginReqDTO loginReqDTO = objectMapper.readValue(new File(LOGIN_REQ_DTO_01), LoginReqDTO.class);
        when(userService.signInAndReturnJWT(loginReqDTO)).thenThrow(RuntimeException.class);

        // then
        try {
            userController.login(loginReqDTO);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).signInAndReturnJWT(loginReqDTO);
    }

    @Test
    @DisplayName("Register - Success Flow")
    void registerSuccess() throws IOException {
        // given
        RegisterReqDTO registerReqDTO = objectMapper.readValue(new File(REGISTER_REQ_DTO_01), RegisterReqDTO.class);
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);

        // when
        when(userService.saveUser(registerReqDTO)).thenReturn(expectedUser);
        BaseResponse<User> registerUser = userController.register(registerReqDTO);

        // then
        assertThat(registerUser.getResult()).isEqualTo(SUCCESS_CODE);
        assertThat(registerUser.getPayload().getId()).isEqualTo(expectedUser.getId());
        assertThat(registerUser.getPayload().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(registerUser.getPayload().getName()).isEqualTo(expectedUser.getName());
        assertThat(registerUser.getPayload().getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(registerUser.getPayload().getContactNo()).isEqualTo(expectedUser.getContactNo());
    }

    @Test
    @DisplayName("Register - Fail Exception Flow")
    void registerFailException() throws IOException {
        // given
        RegisterReqDTO registerReqDTO = objectMapper.readValue(new File(REGISTER_REQ_DTO_01), RegisterReqDTO.class);
        when(userService.saveUser(registerReqDTO)).thenThrow(RuntimeException.class);

        // then
        try {
            userController.register(registerReqDTO);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).saveUser(registerReqDTO);
    }

    @Test
    @DisplayName("Register - Username In Use Flow")
    void registerUsernameInUse() throws IOException {
        // given
        RegisterReqDTO registerReqDTO = objectMapper.readValue(new File(REGISTER_REQ_DTO_01), RegisterReqDTO.class);
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);
        when(userService.saveUser(registerReqDTO)).thenThrow(RuntimeException.class);
        when(userService.findByUsername(registerReqDTO.getUsername())).thenReturn(Optional.of(expectedUser));

        // then
        try {
            userController.register(registerReqDTO);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userService, times(1)).saveUser(registerReqDTO);
    }
}
