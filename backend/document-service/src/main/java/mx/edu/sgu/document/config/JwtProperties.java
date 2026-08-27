package mx.edu.sgu.document.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sgu.jwt")
public record JwtProperties(String secret, String issuer) {
}
