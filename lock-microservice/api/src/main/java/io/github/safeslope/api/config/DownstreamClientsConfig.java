package io.github.safeslope.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DownstreamClientsConfig {

    @Bean
    RestClient skiCardVerificationRestClient(
            @Value("${services.ski-card-verification.base-url}") String baseUrl
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
