package com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk;

import javax.validation.constraints.NotNull;

public class AddServiceTask{
    @NotNull
    private String projectId;
    private String parentTask;
    private String assignee;
    @NotNull
    private String issueTopic;
    private String issueDescription;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getParentTask() {
        return parentTask;
    }

    public void setParentTask(String parentTask) {
        this.parentTask = parentTask;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getIssueTopic() {
        return issueTopic;
    }

    public void setIssueTopic(String issueTopic) {
        this.issueTopic = issueTopic;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    @Override
    public String toString() {
        return "AddServiceTask{" +
                "projectId='" + projectId + '\'' +
                ", parentTask='" + parentTask + '\'' +
                ", assignee='" + assignee + '\'' +
                ", issueTopic='" + issueTopic + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                '}';
    }
}
