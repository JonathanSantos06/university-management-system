package mx.edu.sgu.document.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public ClientHttpRequestFactory documentHttpRequestFactory(ServiceUrlsProperties props) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        int timeout = (int) props.requestTimeoutMs();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return factory;
    }

    @Bean
    public RestClient academicServiceRestClient(ServiceUrlsProperties props, ClientHttpRequestFactory factory) {
        return RestClient.builder()
                .baseUrl(props.academicServiceUrl())
                .requestFactory(factory)
                .build();
    }
}
