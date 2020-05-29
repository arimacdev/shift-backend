package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
public class IdpUserServiceImpl implements IdpUserService {

    private static final Logger logger = LoggerFactory.getLogger(IdpUserServiceImpl.class);

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String DEFAULT_PASSWORD = "123456";

    private static String clientAccessToken = null;
    private final RestTemplate restTemplate;

    public IdpUserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders getHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = String.format("%s:%s", ENVConfig.KEYCLOAK_ROLE_CLIENT_NAME, ENVConfig.KEYCLOAK_ROLE_CLIENT_SECRET);
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        httpHeaders.set("Authorization", "Basic " + new String(encodedAuth));
        return httpHeaders;
    }

    private HttpHeaders getIdpTokenHeader(){
        if (clientAccessToken == null)
            getClientAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + clientAccessToken);
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }

    private void getClientAccessToken(){
        HttpHeaders httpHeaders = getHeader();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(GRANT_TYPE, CLIENT_CREDENTIALS);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.KEYCLOAK_HOST);
        url.append("/auth/realms/");
        url.append(ENVConfig.KEYCLOAK_REALM);
        url.append("/protocol/openid-connect/token");
        logger.info("Access token URL : {}", url);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.POST, new HttpEntity<>(map, httpHeaders), String.class);
        clientAccessToken = new JSONObject(exchange.getBody()).getString("access_token");
    }

    @Override
    public String createUser(UserRegistrationDto userRegistrationDto, String UUID,  boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + clientAccessToken);
            httpHeaders.set("Content-Type", "application/json");
            JSONObject payload = new JSONObject();
            payload.put("username", userRegistrationDto.getUserName());
            payload.put("enabled", true);
            payload.put("email", userRegistrationDto.getEmail());
            JSONArray credentials = new JSONArray();
            JSONObject user = new JSONObject();
            user.put("type", "password");
            user.put("value", userRegistrationDto.getPassword());
            user.put("temporary", true);
            credentials.put(user);
            payload.put("credentials", credentials);
            Map<String,String> attributes = new HashMap<>();
            attributes.put("userId", UUID);
            payload.put("attributes",attributes);

            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), httpHeaders);
            StringBuilder userCreateUrl = new StringBuilder();
            userCreateUrl.append(ENVConfig.KEYCLOAK_HOST);
            userCreateUrl.append("/auth/admin/realms/");
            userCreateUrl.append(ENVConfig.KEYCLOAK_REALM);
            userCreateUrl.append("/users");
            logger.info("User Create URL {}", userCreateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(userCreateUrl.toString(), HttpMethod.POST, entity, String.class);
            String idpUserId =  getIdpUserId(httpHeaders, userRegistrationDto, true);
            setTemporaryPassword(idpUserId, true);

            return idpUserId;

        }
            catch(HttpClientErrorException | HttpServerErrorException e) {
                String response = e.getResponseBodyAsString();
                logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                    getClientAccessToken();
                    return createUser(userRegistrationDto, UUID,false);
                } else if (e.getStatusCode() == HttpStatus.CONFLICT){
                    throw new PMException(new ErrorMessage(ResponseMessage.USERNAME_EXISTS, HttpStatus.CONFLICT));
                }
               throw new PMException(e.getResponseBodyAsString());
             } catch (Exception e) {
                logger.error(e.getMessage());
                throw new PMException(e);
              }
    }

    @Override
    public JSONObject getUserByIdpUserId(String idpUserId, boolean firstRequest) {
        try {
            HttpEntity<Object> userGetEntity = new HttpEntity<>(null, getIdpTokenHeader());
            StringBuilder userRetrieveUrl = new StringBuilder();
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_HOST);
            userRetrieveUrl.append("/auth/admin/realms/");
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_REALM);
            userRetrieveUrl.append("/users/");
            userRetrieveUrl.append(idpUserId);
            logger.info("User Retrieval Url : {}", userRetrieveUrl);
            ResponseEntity<String> idpUser = restTemplate.exchange(userRetrieveUrl.toString(), HttpMethod.GET, userGetEntity, String.class);
            return new JSONObject(idpUser.getBody());
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getUserByIdpUserId(idpUserId, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new PMException(e);
        }
    }

    @Override
    public void updateUserPassword(String idpUserId) {

    }

    @Override
    public void updateUserEmail(String idpUserId, String email, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONObject updatePayload = new JSONObject();
            updatePayload.put("email",email);

            HttpEntity<Object> entity = new HttpEntity<>(updatePayload.toString(), httpHeaders);
            StringBuilder userUpdateUrl = new StringBuilder();
            userUpdateUrl.append(ENVConfig.KEYCLOAK_HOST);
            userUpdateUrl.append("/auth/admin/realms/");
            userUpdateUrl.append(ENVConfig.KEYCLOAK_REALM);
            userUpdateUrl.append("/users/");
            userUpdateUrl.append(idpUserId);
            logger.info("User update URL {}", userUpdateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(userUpdateUrl.toString(), HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                updateUserEmail(idpUserId, email, false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void changeUserActiveSatatus(String idpUserId, boolean status, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = getIdpTokenHeader();
            JSONObject deactivatePayload = new JSONObject();
            deactivatePayload.put("enabled",status);

            HttpEntity<Object> entity = new HttpEntity<>(deactivatePayload.toString(), httpHeaders);
            StringBuilder deactivateUrl = new StringBuilder();
            deactivateUrl.append(ENVConfig.KEYCLOAK_HOST);
            deactivateUrl.append("/auth/admin/realms/");
            deactivateUrl.append(ENVConfig.KEYCLOAK_REALM);
            deactivateUrl.append("/users/");
            deactivateUrl.append(idpUserId);
            logger.info("User Deactivate URL {}", deactivateUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(deactivateUrl.toString(), HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                changeUserActiveSatatus(idpUserId, status,false);
            }
            throw new PMException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }


    private String getIdpUserId(HttpHeaders httpHeaders, UserRegistrationDto userRegistrationDto, boolean firstRequest) {
        try {
            HttpEntity<Object> userGetEntity = new HttpEntity<>(null, httpHeaders);
            StringBuilder userRetrieveUrl = new StringBuilder();
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_HOST);
            userRetrieveUrl.append("/auth/admin/realms/");
            userRetrieveUrl.append(ENVConfig.KEYCLOAK_REALM);
            userRetrieveUrl.append("/users?username=");
            userRetrieveUrl.append(userRegistrationDto.getUserName());
            logger.info("User Retrieval Url : {}", userRetrieveUrl);
            ResponseEntity<String> userResult = restTemplate.exchange(userRetrieveUrl.toString(), HttpMethod.GET, userGetEntity, String.class);
            String response = userResult.getBody();
            String idpUserId = null;
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String userName = jo.getString("username");
                if (userName.equals(userRegistrationDto.getUserName()))
                    idpUserId = jo.getString("id");
            }
            logger.info("Idp userID : {}", idpUserId);
            return idpUserId;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getIdpUserId(httpHeaders, userRegistrationDto, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    private void setTemporaryPassword(String idpUserId, boolean firstRequest){
        try {
            if (clientAccessToken == null)
                getClientAccessToken();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + clientAccessToken);
            httpHeaders.set("Content-Type", "application/json");
            JSONObject credentailsPayload = new JSONObject();
            credentailsPayload.put("type", "password");
            credentailsPayload.put("value", DEFAULT_PASSWORD);
            credentailsPayload.put("temporary", true);

            HttpEntity<Object> entity = new HttpEntity<>(credentailsPayload.toString(), httpHeaders);
            StringBuilder passwordResetUrl = new StringBuilder();
            passwordResetUrl.append(ENVConfig.KEYCLOAK_HOST);
            passwordResetUrl.append("/auth/admin/realms/");
            passwordResetUrl.append(ENVConfig.KEYCLOAK_REALM);
            passwordResetUrl.append("/users/");
            passwordResetUrl.append(idpUserId);
            passwordResetUrl.append("/reset-password");

            logger.info("Password Reset URL {}", passwordResetUrl);
            ResponseEntity<String> exchange = restTemplate.exchange(passwordResetUrl.toString(), HttpMethod.PUT, entity, String.class);
//            return getIdpUserId(httpHeaders, userRegistrationDto, true);
        }
        catch(HttpClientErrorException | HttpServerErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                setTemporaryPassword(idpUserId, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new PMException(e);
        }
    }


}
