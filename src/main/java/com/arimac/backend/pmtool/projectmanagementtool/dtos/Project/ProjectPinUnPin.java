package com.arimac.backend.pmtool.projectmanagementtool.dtos.Project;

import javax.validation.constraints.NotNull;

public class ProjectPinUnPin {
    @NotNull
    private String user;
    @NotNull
    private String project;
    @NotNull
    private boolean isPin;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public boolean getIsPin() {
        return isPin;
    }

    public void setIsPin(boolean pin) {
        isPin = pin;
    }
}
