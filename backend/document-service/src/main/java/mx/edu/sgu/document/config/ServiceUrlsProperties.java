package mx.edu.sgu.document.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sgu.services")
public record ServiceUrlsProperties(String academicServiceUrl, long requestTimeoutMs) {
}
