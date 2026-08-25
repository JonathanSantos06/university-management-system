package mx.edu.sgu.admin;

import mx.edu.sgu.admin.security.JwtValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AdminServiceApplicationTests {

    @Autowired
    private JwtValidator jwtValidator;

    @Test
    void contextLoads() {
        assertThat(jwtValidator).isNotNull();
    }
}
