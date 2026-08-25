package mx.edu.sgu.academic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "sgu.academic")
public record AcademicProperties(BigDecimal passingGrade, short finalPartialNumber) {
}
