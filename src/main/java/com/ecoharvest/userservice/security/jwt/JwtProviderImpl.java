package com.ecoharvest.userservice.security.jwt;

import com.ecoharvest.userservice.security.UserPrincipal;
import com.ecoharvest.userservice.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ecoharvest.userservice.constants.ApplicationConstants.*;

@Component
public class JwtProviderImpl implements JwtProvider {
    @Value("${application.jwt.secret}")
    private String jwtSecret;

    @Value("${application.jwt.expiration}")
    private Long jwtExpiration;

    @Override
    public String generateToken(UserPrincipal auth) {
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim(ROLES, authorities)
                .claim(USER_ID, auth.getId())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest httpServletRequest) {
        Claims claims = extractClaims(httpServletRequest);

        if (claims == null) {
            return null;
        }

        String username = claims.getSubject();
        Long userId = claims.get(USER_ID, Long.class);

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get(ROLES).toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        if (username == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Override
    public boolean isTokenValid(HttpServletRequest httpServletRequest) {
        Claims claims = extractClaims(httpServletRequest);

        if (claims == null || claims.getExpiration().before(new Date())) {
            return false;
        }

        return true;
    }

    private Claims extractClaims(HttpServletRequest httpServletRequest) {
        String token = SecurityUtils.getAuthTokenFromReq(httpServletRequest);

        if (token == null) {
            return null;
        }

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
