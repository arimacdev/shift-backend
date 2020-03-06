package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Service
public class IdpUserServiceImpl implements IdpUserService {

    private static final Logger logger = LoggerFactory.getLogger(IdpUserServiceImpl.class);

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String ACCESS_TOKEN = "access_token";

    private final RestTemplate restTemplate;

    public IdpUserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Object createUser(UserRegistrationDto userRegistrationDto) {
        try {
            String token = getClientAccessToken();
        }
        return null;
    }

    private HttpHeaders getHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = String.format("%s:%s", ENVConfig.KEYCLOAK_ROLE_CLIENT_NAME, ENVConfig.KEYCLOAK_ROLE_CLIENT_SECRET);
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        httpHeaders.set("Authorization", "Basic " + new String(encodedAuth));
        return httpHeaders;
    }

    private String getClientAccessToken(){
        HttpHeaders httpHeaders = getHeader();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(GRANT_TYPE, CLIENT_CREDENTIALS);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.KEYCLOAK_HOST);
        url.append("auth/realms/");
        url.append(ENVConfig.KEYCLOAK_REALM);
        url.append("/protocol/openid-connect/token");
        logger.info("Access token URL : {}", url);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.POST, new HttpEntity<>(map, httpHeaders), String.class);
        return new JSONObject(exchange.getBody()).getString("access_token");
    }
}
