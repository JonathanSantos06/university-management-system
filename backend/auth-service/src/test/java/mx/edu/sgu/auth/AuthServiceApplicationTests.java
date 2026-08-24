package mx.edu.sgu.auth;

import mx.edu.sgu.auth.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceApplicationTests {

    @Autowired
    private JwtService jwtService;

    @Test
    void contextLoads() {
        assertThat(jwtService).isNotNull();
    }

    @Test
    void refreshTokenValuesAreUniqueAndOpaque() {
        String a = jwtService.generateOpaqueRefreshTokenValue();
        String b = jwtService.generateOpaqueRefreshTokenValue();
        assertThat(a).isNotEqualTo(b);
        assertThat(jwtService.hashToken(a)).isNotEqualTo(jwtService.hashToken(b));
    }
}
