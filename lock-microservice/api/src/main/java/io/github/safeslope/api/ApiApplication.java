package io.github.safeslope.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.safeslope")
@ConfigurationPropertiesScan(basePackages = "io.github.safeslope.mqtt.config")
@EnableJpaRepositories(basePackages = "io.github.safeslope")
@EntityScan(basePackages = "io.github.safeslope.entities")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}