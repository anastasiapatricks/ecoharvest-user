package com.ecoharvest.userservice.service;

import com.ecoharvest.userservice.dto.LoginReqDTO;
import com.ecoharvest.userservice.dto.RegisterReqDTO;
import com.ecoharvest.userservice.exception.UserException;
import com.ecoharvest.userservice.model.User;
import com.ecoharvest.userservice.repository.UserRepository;
import com.ecoharvest.userservice.security.UserPrincipal;
import com.ecoharvest.userservice.security.jwt.JwtProvider;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ecoharvest.userservice.constants.TestConstants.*;
import static com.ecoharvest.userservice.constants.TestConstants.USER_02;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOG = LogManager.getLogger(UserServiceTest.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DirtiesContext
    @DisplayName("Save User")
    void saveUser() throws IOException {
        // given
        RegisterReqDTO testData = objectMapper.readValue(new File(REGISTER_REQ_DTO_01), RegisterReqDTO.class);
        User testUser = objectMapper.readValue(new File(USER_01), User.class);

        given(modelMapper.map(testData, User.class)).willReturn(testUser);

        // when
        User user = userService.saveUser(testData);

        // then
        assertThat(user.getId()).isEqualTo(testUser.getId());
        assertThat(user.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(user.getName()).isEqualTo(testUser.getName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getContactNo()).isEqualTo(testUser.getContactNo());
    }

    @Test
    @DirtiesContext
    @DisplayName("Find By Username")
    void findByUsername() throws IOException {
        // given
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);

        given(userRepository.findByUsername(USERNAME)).willReturn(Optional.of(expectedUser));

        // when
        Optional<User> user = userService.findByUsername(USERNAME);

        // then
        assertThat(user.get().getId()).isEqualTo(expectedUser.getId());
        assertThat(user.get().getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(user.get().getName()).isEqualTo(expectedUser.getName());
        assertThat(user.get().getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(user.get().getContactNo()).isEqualTo(expectedUser.getContactNo());
    }

    @Test
    @DirtiesContext
    @DisplayName("Retrieve User List")
    void retrieveUserList() throws IOException {
        // given
        User expectedUser_01 = objectMapper.readValue(new File(USER_01), User.class);
        User expectedUser_02 = objectMapper.readValue(new File(USER_02), User.class);

        List<User> userList = new ArrayList<>();
        userList.add(expectedUser_01);
        userList.add(expectedUser_02);

        given(userRepository.getUserList()).willReturn(userList);

        // when
        List<User> user = userService.retrieveUserList();

        // then
        assertThat(user.get(0).getId()).isEqualTo(expectedUser_01.getId());
        assertThat(user.get(0).getUsername()).isEqualTo(expectedUser_01.getUsername());
        assertThat(user.get(1).getId()).isEqualTo(expectedUser_02.getId());
        assertThat(user.get(1).getUsername()).isEqualTo(expectedUser_02.getUsername());
    }

    @Test
    @DirtiesContext
    @DisplayName("Retrieve User By Id - Success Flow")
    void retrieveUserByIdSuccess() throws IOException {
        // given
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);

        given(userRepository.findById(ID)).willReturn(expectedUser);

        // when
        User user = userService.retrieveUserById(ID);

        // then
        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(user.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(user.getName()).isEqualTo(expectedUser.getName());
        assertThat(user.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(user.getContactNo()).isEqualTo(expectedUser.getContactNo());
    }

    @Test
    @DirtiesContext
    @DisplayName("Retrieve User By Id - Fail Flow")
    void retrieveUserByIdFail() throws UserException {
        // given
        given(userRepository.findById(ID)).willReturn(null);

        // when
        try {
            userService.retrieveUserById(ID);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userRepository, times(1)).findById(ID);
    }

    @Test
    @DirtiesContext
    @DisplayName("Update User Details - Success Flow")
    void updateUserDetailsSuccess() throws IOException {
        // given
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);
        expectedUser.setName(CHANGED_NAME);
        expectedUser.setEmail(CHANGED_EMAIL);
        expectedUser.setContactNo(CHANGED_CONTACT_NO);

        given(userRepository.findById(ID)).willReturn(expectedUser);

        // when
        User user = userService.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO);

        // then
        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(user.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(user.getName()).isEqualTo(CHANGED_NAME);
        assertThat(user.getEmail()).isEqualTo(CHANGED_EMAIL);
        assertThat(user.getContactNo()).isEqualTo(CHANGED_CONTACT_NO);
    }

    @Test
    @DirtiesContext
    @DisplayName("Update User Details - Fail Flow")
    void updateUserDetailsFail() throws UserException {
        // given
        given(userRepository.findById(ID)).willReturn(null);

        // when
        try {
            userService.updateUserDetails(ID, CHANGED_NAME, CHANGED_EMAIL, CHANGED_CONTACT_NO);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userRepository, times(1)).findById(ID);
    }

    @Test
    @DirtiesContext
    @DisplayName("Change Password - Success Flow")
    void changePasswordSuccess() throws IOException {
        // given
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);
        String encryptedPassword = passwordEncoder.encode(CHANGED_PWD);
        expectedUser.setPassword(encryptedPassword);

        given(passwordEncoder.encode(CHANGED_PWD)).willReturn(encryptedPassword);
        given(userRepository.findById(ID)).willReturn(expectedUser);

        // when
        User user = userService.changePassword(ID, CHANGED_PWD);

        // then
        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(user.getPassword()).isEqualTo(encryptedPassword);
    }

    @Test
    @DirtiesContext
    @DisplayName("Change Password - Fail Flow")
    void changePasswordFail() throws UserException {
        // given
        String encryptedPassword = passwordEncoder.encode(CHANGED_PWD);
        given(passwordEncoder.encode(CHANGED_PWD)).willReturn(encryptedPassword);
        given(userRepository.findById(ID)).willReturn(null);

        // when
        try {
            userService.changePassword(ID, CHANGED_PWD);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userRepository, times(1)).findById(ID);
    }

    @Test
    @DirtiesContext
    @DisplayName("Delete User - Success Flow")
    void deleteUserSuccess() throws IOException {
        // given
        User expectedUser_01 = objectMapper.readValue(new File(USER_01), User.class);
        User expectedUser_02 = objectMapper.readValue(new File(USER_02), User.class);

        List<User> userList = new ArrayList<>();
        userList.add(expectedUser_02);

        given(userRepository.findById(ID)).willReturn(expectedUser_01);
        given(userRepository.getUserList()).willReturn(userList);

        // when
        List<User> user = userService.deleteUser(ID);

        // then
        assertThat(user.get(0).getId()).isEqualTo(expectedUser_02.getId());
        assertThat(user.get(0).getUsername()).isEqualTo(expectedUser_02.getUsername());
        assertThat(user.get(0).getName()).isEqualTo(expectedUser_02.getName());
        assertThat(user.get(0).getEmail()).isEqualTo(expectedUser_02.getEmail());
        assertThat(user.get(0).getContactNo()).isEqualTo(expectedUser_02.getContactNo());
    }

    @Test
    @DirtiesContext
    @DisplayName("Delete User - Fail Flow")
    void deleteUserFail() throws UserException {
        // given
        given(userRepository.findById(ID)).willReturn(null);

        // when
        try {
            userService.deleteUser(ID);
        } catch (UserException e) {
            LOG.error(e.getMessage());
        }

        // then
        verify(userRepository, times(1)).findById(ID);
    }

    @Test
    @DirtiesContext
    @DisplayName("Sign In And Return JWT")
    void signInAndReturnJWT() throws IOException {
        // given
        LoginReqDTO loginReqDTO = objectMapper.readValue(new File(LOGIN_REQ_DTO_01), LoginReqDTO.class);
        User expectedUser = objectMapper.readValue(new File(USER_01), User.class);
        UserPrincipal userPrincipal = objectMapper.readValue(new File(USER_PRINCIPAL), UserPrincipal.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null);

        given(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReqDTO.getUsername(), loginReqDTO.getPassword())
        )).willReturn(authentication);

        given(jwtProvider.generateToken(userPrincipal)).willReturn(TOKEN);

        // when
        User user = userService.signInAndReturnJWT(loginReqDTO);

        // then
        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(user.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(user.getName()).isEqualTo(expectedUser.getName());
        assertThat(user.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(user.getContactNo()).isEqualTo(expectedUser.getContactNo());
    }
}
