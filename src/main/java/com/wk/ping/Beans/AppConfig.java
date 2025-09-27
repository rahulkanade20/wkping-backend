package com.wk.ping.Beans;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))  // Connection timeout: 5 seconds
                .setReadTimeout(Duration.ofSeconds(10))    // Read timeout: 10 seconds
                .build();
    }
}
