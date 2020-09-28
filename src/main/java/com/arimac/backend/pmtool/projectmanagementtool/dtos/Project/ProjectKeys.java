package com.arimac.backend.pmtool.projectmanagementtool.dtos.Project;

import javax.validation.constraints.NotNull;

public class ProjectKeys {
    @NotNull
    private String admin;
    private String projectKey;
    @NotNull
    private String domain;
    @NotNull
    private boolean isValid;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean getValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
