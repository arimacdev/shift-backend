package com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class RequestKey {
    @NotNull
    private String projectKey;
    @NotNull
    @Email(message = "Please enter a valid email address")
    private String email;

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
