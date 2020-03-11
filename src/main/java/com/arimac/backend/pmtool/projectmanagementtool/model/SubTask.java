package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubTask implements RowMapper<SubTask> {

    private String subtaskId;
    private String taskId;
    private String subtaskName;
    private boolean subtaskStatus;

    public SubTask() {
    }

    public SubTask(String subtaskId, String taskId, String subtaskName, boolean subtaskStatus) {
        this.subtaskId = subtaskId;
        this.taskId = taskId;
        this.subtaskName = subtaskName;
        this.subtaskStatus = subtaskStatus;
    }

    public String getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(String subtaskId) {
        this.subtaskId = subtaskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSubtaskName() {
        return subtaskName;
    }

    public void setSubtaskName(String subtaskName) {
        this.subtaskName = subtaskName;
    }

    public boolean isSubtaskStatus() {
        return subtaskStatus;
    }

    public void setSubtaskStatus(boolean subtaskStatus) {
        this.subtaskStatus = subtaskStatus;
    }


    @Override
    public SubTask mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SubTask(
                resultSet.getString("subtaskId"),
                resultSet.getString("taskId"),
                resultSet.getString("subtaskName"),
                resultSet.getBoolean("subtaskStatus")
        );
    }
}
