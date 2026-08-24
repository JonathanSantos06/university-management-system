package mx.edu.sgu.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mx.edu.sgu.auth.config.JwtProperties;
import mx.edu.sgu.auth.domain.Role;
import mx.edu.sgu.auth.domain.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Genera y valida access tokens (JWT stateless) y produce/verifica el valor
 * opaco del refresh token (que se persiste solo como hash en BD).
 */
@Service
public class JwtService {

    private final JwtProperties properties;
    private final SecretKey signingKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        byte[] keyBytes = Base64.getDecoder().decode(properties.secret());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(properties.accessTokenExpirationMinutes(), ChronoUnit.MINUTES);

        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(user.getUsername())
                .claim("uid", user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .issuedAt(java.util.Date.from(now))
                .expiration(java.util.Date.from(expiry))
                .signWith(signingKey)
                .compact();
    }

    public long getAccessTokenExpirationSeconds() {
        return properties.accessTokenExpirationMinutes() * 60;
    }

    public Claims parseAndValidate(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Valor opaco enviado al cliente como refresh token. */
    public String generateOpaqueRefreshTokenValue() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public long getRefreshTokenExpirationDays() {
        return properties.refreshTokenExpirationDays();
    }

    /** Solo se persiste el hash SHA-256 del refresh token, nunca el valor en claro. */
    public String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
