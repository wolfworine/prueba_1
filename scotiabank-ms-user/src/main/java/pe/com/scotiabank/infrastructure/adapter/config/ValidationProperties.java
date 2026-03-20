package pe.com.scotiabank.infrastructure.adapter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "validation")
@Getter
@Setter
public class ValidationProperties {

    private String passwordRegex;
}
