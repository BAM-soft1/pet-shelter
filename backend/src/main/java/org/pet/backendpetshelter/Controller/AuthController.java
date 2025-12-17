package org.pet.backendpetshelter.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.pet.backendpetshelter.Configuration.JwtProperties;
import org.pet.backendpetshelter.DTO.AuthResponse;
import org.pet.backendpetshelter.DTO.LoginRequest;
import org.pet.backendpetshelter.DTO.RegisterUserRequest;
import org.pet.backendpetshelter.DTO.UserResponse;
import org.pet.backendpetshelter.Service.AuthService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Profile("mysql")
public class AuthController {

    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String ACCESS_COOKIE = "access_token";

    private final AuthService authService;
    private final long refreshMaxAge;
    private final long accessMaxAge;
    private final boolean cookieSecure;

    public AuthController(AuthService authService, JwtProperties props) {
        this.authService = authService;
        this.refreshMaxAge = props.getRefreshExpirationSeconds();
        this.accessMaxAge = props.getAccessExpirationSeconds();
        this.cookieSecure = props.isCookieSecure();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        var user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse res) {
        var pair = authService.loginIssueTokens(request);

        // Set access token cookie
        ResponseCookie access = ResponseCookie.from(ACCESS_COOKIE, pair.getAccessToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(accessMaxAge)
                .sameSite("Strict")
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, access.toString());

        // Set refresh token cookie
        ResponseCookie refresh = ResponseCookie.from(REFRESH_COOKIE, pair.getRefreshToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(refreshMaxAge)
                .sameSite("Strict")      
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());

        return ResponseEntity.ok()
                .body(new AuthResponse(pair.getAccessToken(), "Bearer", pair.getAccessExpiresInSeconds()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest req, HttpServletResponse res) {
        String refreshToken = extractRefreshCookie(req);
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var result = authService.rotateRefreshToken(refreshToken);

        // Set new access token cookie
        ResponseCookie access = ResponseCookie.from(ACCESS_COOKIE, result.getAccessToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(accessMaxAge)
                .sameSite("Strict")
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, access.toString());

        // Set new refresh token cookie
        ResponseCookie refresh = ResponseCookie.from(REFRESH_COOKIE, result.getRefreshToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(refreshMaxAge)
                .sameSite("Strict")
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());

        return ResponseEntity.ok(new AuthResponse(result.getAccessToken(), "Bearer", authService.getAccessExpSeconds()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res) {
        String accessToken = extractAccessCookie(req);
        String refreshToken = extractRefreshCookie(req);

        authService.logout(accessToken, refreshToken);

        // Clear access token cookie
        ResponseCookie deleteAccess = ResponseCookie.from(ACCESS_COOKIE, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());

        // Clear refresh token cookie
        ResponseCookie deleteRefresh = ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, deleteRefresh.toString());

        return ResponseEntity.noContent().build();
    }

    private String extractAccessCookie(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        for (Cookie c : req.getCookies()) {
            if (ACCESS_COOKIE.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    private String extractRefreshCookie(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        for (Cookie c : req.getCookies()) {
            if (REFRESH_COOKIE.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}