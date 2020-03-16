package com.arimac.backend.pmtool.projectmanagementtool.config;

//import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
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

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(ENVConfig.AWS_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ENVConfig.AWS_ACCESS_KEY, ENVConfig.AWS_SECRET_KEY)))
                .build();
    }


}
