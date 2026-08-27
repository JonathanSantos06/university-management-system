package mx.edu.sgu.document;

import mx.edu.sgu.document.config.JwtProperties;
import mx.edu.sgu.document.config.ServiceUrlsProperties;
import mx.edu.sgu.document.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, StorageProperties.class, ServiceUrlsProperties.class})
public class DocumentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentServiceApplication.class, args);
    }
}
