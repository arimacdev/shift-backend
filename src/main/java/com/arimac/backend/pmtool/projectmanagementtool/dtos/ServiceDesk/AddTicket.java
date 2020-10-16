package com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class AddTicket {
    @NotNull
    private String projectKey;
    @NotNull
    @Email(message = "Please enter a valid email address")
    private String email;
    @NotNull
    private String issueTopic;
    @NotNull
    private Integer severity;

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

    public String getIssueTopic() {
        return issueTopic;
    }

    public void setIssueTopic(String issueTopic) {
        this.issueTopic = issueTopic;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }
}
