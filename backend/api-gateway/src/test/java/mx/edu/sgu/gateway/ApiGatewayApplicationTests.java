package mx.edu.sgu.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring Cloud Gateway levanta correctamente
        // con las rutas definidas en application.yml.
    }
}
