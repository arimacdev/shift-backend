package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportMember;

import javax.validation.constraints.NotNull;

public class AddSupportMember {
    @NotNull
    private String projectId;
    @NotNull
    private String memberId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "AddSupportMember{" +
                "projectId='" + projectId + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
