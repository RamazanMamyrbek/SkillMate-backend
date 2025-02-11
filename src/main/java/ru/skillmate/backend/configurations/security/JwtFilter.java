package ru.skillmate.backend.configurations.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.skillmate.backend.dto.errors.ErrorResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String accessToken = extractCookie(request, ACCESS_TOKEN);
        String refreshToken = extractCookie(request, REFRESH_TOKEN);
        try {
            if(accessToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if(jwtProvider.validateToken(accessToken)) {
                    authenticateUser(accessToken);
                }  else if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
                    String newAccessToken = jwtProvider.refreshAccessToken(refreshToken);
                    setCookie(response, ACCESS_TOKEN, newAccessToken);
                    authenticateUser(newAccessToken);
                }
            }
        } catch (Exception ex) {
            ErrorResponseDto responseDto = new ErrorResponseDto(
                    request.getPathInfo(),
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage(),
                    LocalDateTime.now()
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String accessToken) {
        String username = jwtProvider.extractUsername(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractCookie(HttpServletRequest request, String cookieName) {
        if(request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .map(cookie -> cookie.getValue())
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private void setCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtProvider.getAccessKeyExpiration());
        response.addCookie(cookie);
    }

}