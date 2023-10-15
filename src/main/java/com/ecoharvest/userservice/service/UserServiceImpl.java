package com.ecoharvest.userservice.service;

import com.ecoharvest.userservice.dto.*;
import com.ecoharvest.userservice.exception.UserException;
import com.ecoharvest.userservice.model.User;
import com.ecoharvest.userservice.repository.UserRepository;
import com.ecoharvest.userservice.security.UserPrincipal;
import com.ecoharvest.userservice.security.jwt.JwtProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ecoharvest.userservice.constants.ApplicationConstants.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User saveUser(RegisterReqDTO registerReqDTO) {
        registerReqDTO.setPassword(passwordEncoder.encode(registerReqDTO.getPassword()));

        User user = modelMapper.map(registerReqDTO, User.class);
        user.setCreatedDateTime(LocalDateTime.now());
        userRepository.save(user);

        user.setToken("");

        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> retrieveUserList() {
        return userRepository.getUserList();
    }

    @Override
    public User retrieveUserById(Long id) throws UserException {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new UserException(ID_NOT_FOUND_ERR_MSG);
        }

        user.setToken("");

        return user;
    }

    @Override
    @Transactional
    public User updateUserDetails(Long id, String name, String email, String contactNo) throws UserException {
        userRepository.updateUserDetails(id, name, email, contactNo);
        User user = userRepository.findById(id);

        if (user == null) {
            throw new UserException(ID_NOT_FOUND_ERR_MSG);
        }

        user.setToken("");

        return user;
    }

    @Override
    @Transactional
    public User changePassword(Long id, String password) throws UserException {
        String encryptedPassword = passwordEncoder.encode(password);

        userRepository.changePassword(id, encryptedPassword);
        User user = userRepository.findById(id);

        if (user == null) {
            throw new UserException(ID_NOT_FOUND_ERR_MSG);
        }

        user.setToken("");

        return user;
    }

    @Override
    @Transactional
    public List<User> deleteUser(Long id) throws UserException {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new UserException(ID_NOT_FOUND_ERR_MSG);
        }

        userRepository.delete(user);

        return userRepository.getUserList();
    }

    @Override
    public User signInAndReturnJWT(LoginReqDTO loginReqDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReqDTO.getUsername(), loginReqDTO.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrincipal);

        User loginUser = userPrincipal.getUser();
        loginUser.setToken(jwt);

        return loginUser;
    }
}
