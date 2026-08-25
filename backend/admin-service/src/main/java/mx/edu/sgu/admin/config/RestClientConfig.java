package mx.edu.sgu.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Clientes HTTP síncronos (RestClient, Spring 6.1+) hacia los microservicios que
 * admin-service agrega. admin-service no persiste nada propio: solo consulta.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public ClientHttpRequestFactory adminHttpRequestFactory(ServiceUrlsProperties props) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        int timeout = (int) props.requestTimeoutMs();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return factory;
    }

    @Bean
    public RestClient studentServiceRestClient(ServiceUrlsProperties props, ClientHttpRequestFactory factory) {
        return RestClient.builder()
                .baseUrl(props.studentServiceUrl())
                .requestFactory(factory)
                .build();
    }

    @Bean
    public RestClient academicServiceRestClient(ServiceUrlsProperties props, ClientHttpRequestFactory factory) {
        return RestClient.builder()
                .baseUrl(props.academicServiceUrl())
                .requestFactory(factory)
                .build();
    }

    @Bean
    public RestClient documentServiceRestClient(ServiceUrlsProperties props, ClientHttpRequestFactory factory) {
        return RestClient.builder()
                .baseUrl(props.documentServiceUrl())
                .requestFactory(factory)
                .build();
    }
}
