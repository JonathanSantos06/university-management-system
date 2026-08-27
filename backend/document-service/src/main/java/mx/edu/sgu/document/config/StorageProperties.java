package mx.edu.sgu.document.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sgu.storage")
public record StorageProperties(String basePath, List<String> allowedContentTypes) {
}
