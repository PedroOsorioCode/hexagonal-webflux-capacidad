package com.microservicio.hxcapacidad.infrastructure.out.webclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientExternalConfig {
    @Value("${addressservice.base.url}")
    private String addressBaseUrl;

    @Value("${addressservice.base.urlbootcamp}")
    private String addressBaseUrlBootcamp;

    @Bean
    public WebClient webClientExternal() {
        return WebClient.builder().baseUrl(addressBaseUrl).build();
    }

    @Bean
    public WebClient webClientBootcampExternal() {
        return WebClient.builder().baseUrl(addressBaseUrlBootcamp).build();
    }
}
