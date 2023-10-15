package com.ecoharvest.userservice.security.jwt;

import com.ecoharvest.userservice.security.UserPrincipal;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface JwtProvider {
    String generateToken(UserPrincipal auth);

    Authentication getAuthentication(HttpServletRequest httpServletRequest);

    boolean isTokenValid(HttpServletRequest httpServletRequest);
}
