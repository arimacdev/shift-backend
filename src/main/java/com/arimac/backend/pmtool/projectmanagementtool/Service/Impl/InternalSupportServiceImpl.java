package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalSupportService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.CreateSupportProject;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.UpdateStatus;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InternalSupportServiceImpl implements InternalSupportService {
    private final RestTemplate restTemplate;

    public InternalSupportServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public void createSupportProject(CreateSupportProject createSupportProject) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(createSupportProject, httpHeaders);
        restTemplate.exchange("http://localhost:8081/api/support-service/internal/project", HttpMethod.POST, httpEntity, String.class);
    }

    @Override
    public void updateSupportProject(UpdateStatus updateStatus) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(updateStatus, httpHeaders);
        restTemplate.exchange("http://localhost:8081/api/support-service/internal/project", HttpMethod.PUT, httpEntity, String.class);
    }

    @Override
    public Object createAdminForSupportProject(String project, AddSupportUserDto addSupportUserDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("isInternal", "true");
        httpHeaders.add("project", project);
        HttpEntity<Object> httpEntity = new HttpEntity<>(addSupportUserDto, httpHeaders);
       return restTemplate.exchange("http://localhost:8081/api/support-service/internal/user/admin", HttpMethod.POST, httpEntity, String.class);
    }

    @Override
    public SupportUser getSupportUserByEmail(String email) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("isInternal", "true");
        httpHeaders.add("user", "internal");
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        String user =  restTemplate.exchange("http://localhost:8081/api/support-service/user?email=" + email, HttpMethod.GET, httpEntity, String.class).getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(new JSONObject(user).get("data").toString(), SupportUser.class);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
