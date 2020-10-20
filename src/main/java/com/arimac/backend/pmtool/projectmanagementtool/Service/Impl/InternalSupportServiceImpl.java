package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalSupportService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.CreateSupportProject;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.UpdateStatus;
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
}
