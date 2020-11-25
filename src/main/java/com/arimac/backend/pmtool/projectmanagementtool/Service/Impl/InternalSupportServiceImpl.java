package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalSupportService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportMemberResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.*;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
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
import java.util.List;

@Service
public class InternalSupportServiceImpl implements InternalSupportService {
    private static final Logger logger = LoggerFactory.getLogger(IdpUserServiceImpl.class);
    private static String clientAccessToken = null;
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private final RestTemplate restTemplate;

    public InternalSupportServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public void createSupportProject(CreateSupportProject createSupportProject, boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
            HttpEntity<Object> httpEntity = new HttpEntity<>(createSupportProject, httpHeaders);
            restTemplate.exchange("http://localhost:8081/api/support-service/internal/project", HttpMethod.POST, httpEntity, String.class);
        } catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                 createSupportProject(createSupportProject,  false);
            }
            throw new PMException(e.getResponseBodyAsString());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void updateSupportProject(UpdateStatus updateStatus, boolean firstRequest) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
            HttpEntity<Object> httpEntity = new HttpEntity<>(updateStatus, httpHeaders);
            restTemplate.exchange("http://localhost:8081/api/support-service/internal/project", HttpMethod.PUT, httpEntity, String.class);
        }  catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                updateSupportProject(updateStatus,  false);
            }
            throw new PMException(e.getResponseBodyAsString());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public Object createAdminForSupportProject(String project, AddSupportUserDto addSupportUserDto, boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
            httpHeaders.add("isInternal", "true");
            httpHeaders.add("project", project);
            HttpEntity<Object> httpEntity = new HttpEntity<>(addSupportUserDto, httpHeaders);
            return restTemplate.exchange("http://localhost:8081/api/support-service/internal/user/admin", HttpMethod.POST, httpEntity, String.class);
        } catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return createAdminForSupportProject(project, addSupportUserDto, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public SupportUser getSupportUserByEmail(String email, boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
        httpHeaders.add("isInternal", "true");
        httpHeaders.add("user", "internal");
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        String user =  restTemplate.exchange("http://localhost:8081/api/support-service/user?email=" + email, HttpMethod.GET, httpEntity, String.class).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(new JSONObject(user).get("data").toString(), SupportUser.class);
        }
        catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getSupportUserByEmail(email, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<SupportUser> getSupportUsersByOrganization(String organization, boolean firstRequest) {
        try {
        if (clientAccessToken == null)
            getClientAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
        httpHeaders.add("isInternal", "true");
        httpHeaders.add("user", "internal");
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        String userList =  restTemplate.exchange("http://localhost:8081/api/support-service/user/organization/" + organization, HttpMethod.GET, httpEntity, String.class).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(new JSONObject(userList).get("data").toString(), new TypeReference<List<SupportUser>>(){});
        } catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getSupportUsersByOrganization(organization, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<SupportMemberResponse> getSupportUsersByProject(String projectId, boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("isInternal", "true");
        httpHeaders.add("user", "internal");
        httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        String userList =  restTemplate.exchange("http://localhost:8081/api/support-service/user/project/" + projectId, HttpMethod.GET, httpEntity, String.class).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(new JSONObject(userList).get("data").toString(), new TypeReference<List<SupportMemberResponse>>(){});
        }
        catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getSupportUsersByProject(projectId, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public ServiceTicketStatus getSupportTicketStatusByProject(String userId, String projectId, boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("userId", userId);
            httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
            HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
            String ticketStatus =  restTemplate.exchange("http://localhost:8081/api/support-service/internal/ticket/project/" + projectId + "/status", HttpMethod.GET, httpEntity, String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(new JSONObject(ticketStatus).get("data").toString(), ServiceTicketStatus.class);
        }
        catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getSupportTicketStatusByProject(userId, projectId, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<ServiceTicketUser> getSupportTicketsByProject(String projectId, int startIndex, int limit, boolean firstRequest) {
        try {
            if (clientAccessToken == null)
                getClientAccessToken();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + clientAccessToken);
            HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
            String userList =  restTemplate.exchange("http://localhost:8081/api/support-service/internal/ticket/project/" + projectId + "?startIndex="+ startIndex + "&limit=" + limit, HttpMethod.GET, httpEntity, String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(new JSONObject(userList).get("data").toString(), new TypeReference<List<ServiceTicketUser>>(){});
        }
        catch(HttpClientErrorException e) {
            String response = e.getResponseBodyAsString();
            logger.error("Error response | Status : {} Response: {}", e.getStatusCode(), response);
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && firstRequest) {
                getClientAccessToken();
                return getSupportTicketsByProject(projectId, startIndex, limit, false);
            }
            throw new PMException(e.getResponseBodyAsString());
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    private void getClientAccessToken(){
        HttpHeaders httpHeaders = getHeader();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(GRANT_TYPE, CLIENT_CREDENTIALS);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.KEYCLOAK_HOST);
        url.append("/auth/realms/");
        url.append(ENVConfig.SUPPORT_REALM);
        url.append("/protocol/openid-connect/token");
        logger.info("Access token URL : {}", url);
        ResponseEntity<String> exchange = restTemplate.exchange(url.toString(), HttpMethod.POST, new HttpEntity<>(map, httpHeaders), String.class);
        clientAccessToken = new JSONObject(exchange.getBody()).getString("access_token");
    }

    private HttpHeaders getHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = String.format("%s:%s", ENVConfig.SUPPORT_CLIENT_NAME, ENVConfig.SUPPORT_CLIENT_SECRET);
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        httpHeaders.set("Authorization", "Basic " + new String(encodedAuth));
        return httpHeaders;
    }
}
