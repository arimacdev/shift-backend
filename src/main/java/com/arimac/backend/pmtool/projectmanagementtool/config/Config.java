package com.arimac.backend.pmtool.projectmanagementtool.config;

//import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

//    @Bean
//    public KeycloakClientRequestFactory keyConfig(){
//        return new KeycloakClientRequestFactory();
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
