package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRelationship implements RowMapper<TaskRelationship> {
    @NotNull
    private String projectId;
    @NotNull
    private String fromLink;
    @NotNull
    private String toLink;

    public TaskRelationship() {
    }

    public TaskRelationship(String projectId, String fromLink, String toLink) {
        this.projectId = projectId;
        this.fromLink = fromLink;
        this.toLink = toLink;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFromLink() {
        return fromLink;
    }

    public void setFromLink(String fromLink) {
        this.fromLink = fromLink;
    }

    public String getToLink() {
        return toLink;
    }

    public void setToLink(String toLink) {
        this.toLink = toLink;
    }

    @Override
    public TaskRelationship mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskRelationship(
                resultSet.getString("projectId"),
                resultSet.getString("fromLink"),
                resultSet.getString("toLink")
        );
    }

    @Override
    public String toString() {
        return "TaskRelationship{" +
                "projectId='" + projectId + '\'' +
                ", fromLink='" + fromLink + '\'' +
                ", toLink='" + toLink + '\'' +
                '}';
    }

}
