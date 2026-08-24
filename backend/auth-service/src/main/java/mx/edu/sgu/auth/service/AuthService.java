package mx.edu.sgu.auth.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.auth.domain.RefreshToken;
import mx.edu.sgu.auth.domain.User;
import mx.edu.sgu.auth.dto.request.LoginRequest;
import mx.edu.sgu.auth.dto.response.AuthResponse;
import mx.edu.sgu.auth.dto.response.UserResponse;
import mx.edu.sgu.auth.exception.InvalidCredentialsException;
import mx.edu.sgu.auth.exception.TokenRefreshException;
import mx.edu.sgu.auth.repository.RefreshTokenRepository;
import mx.edu.sgu.auth.repository.UserRepository;
import mx.edu.sgu.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Usuario o contraseña incorrectos");
        }
        if (!user.isActive()) {
            throw new InvalidCredentialsException("El usuario está inactivo");
        }
        if (user.isLocked()) {
            throw new InvalidCredentialsException("El usuario está bloqueado");
        }

        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);

        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {
        String hash = jwtService.hashToken(rawRefreshToken);
        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new TokenRefreshException("Refresh token inválido"));

        if (!stored.isUsable()) {
            throw new TokenRefreshException("Refresh token expirado o revocado");
        }

        // Rotación: se revoca el token usado y se emite uno nuevo
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        return issueTokens(stored.getUser());
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        String hash = jwtService.hashToken(rawRefreshToken);
        refreshTokenRepository.findByTokenHash(hash).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String rawRefreshToken = jwtService.generateOpaqueRefreshTokenValue();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(jwtService.hashToken(rawRefreshToken))
                .expiresAt(OffsetDateTime.now().plusDays(jwtService.getRefreshTokenExpirationDays()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.of(accessToken, rawRefreshToken,
                jwtService.getAccessTokenExpirationSeconds(), UserResponse.from(user));
    }
}
