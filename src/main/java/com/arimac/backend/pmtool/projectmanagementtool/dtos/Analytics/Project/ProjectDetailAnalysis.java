package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;

import java.util.Date;
import java.util.List;

public class ProjectDetailAnalysis {
    private String projectId;
    private String projectName;
    private Date projectCreatedDate;
    private ProjectStatusEnum projectStatus;
    private int taskCount;
    private int closedCount;
    private int memberCount;
    private List<User> owners;
    private int engagement;
    private long timeTaken;


    public ProjectDetailAnalysis() {
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getProjectStartDate() {
        return projectCreatedDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectCreatedDate = projectStartDate;
    }

    public ProjectStatusEnum getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatusEnum projectStatus) {
        this.projectStatus = projectStatus;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
        this.owners = owners;
    }

    public int getEngagement() {
        return engagement;
    }

    public void setEngagement(int engagement) {
        this.engagement = engagement;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getClosedCount() {
        return closedCount;
    }

    public void setClosedCount(int closedCount) {
        this.closedCount = closedCount;
    }
}
