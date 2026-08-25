package mx.edu.sgu.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sgu.services")
public record ServiceUrlsProperties(
        String studentServiceUrl,
        String academicServiceUrl,
        String documentServiceUrl,
        long requestTimeoutMs
) {
}
