package com.arimac.backend.pmtool.projectmanagementtool.model;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskFile implements RowMapper<TaskFile> {
    private String taskFileId;
    private String taskId;
    private String taskFileName;
    private String taskFileUrl;
    private String taskFileCreator;
    private Timestamp taskFileDate;

    public TaskFile() {
    }

    public TaskFile(String taskFileId, String taskId, String taskFileName, String taskFileUrl, String taskFileCreator, Timestamp taskFileDate) {
        this.taskFileId = taskFileId;
        this.taskId = taskId;
        this.taskFileName = taskFileName;
        this.taskFileUrl = taskFileUrl;
        this.taskFileCreator = taskFileCreator;
        this.taskFileDate = taskFileDate;
    }

    public String getTaskFileId() {
        return taskFileId;
    }

    public void setTaskFileId(String taskFileId) {
        this.taskFileId = taskFileId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskFileName() {
        return taskFileName;
    }

    public void setTaskFileName(String taskFileName) {
        this.taskFileName = taskFileName;
    }

    public String getTaskFileUrl() {
        return taskFileUrl;
    }

    public void setTaskFileUrl(String taskFileUrl) {
        this.taskFileUrl = taskFileUrl;
    }

    public String getTaskFileCreator() {
        return taskFileCreator;
    }

    public void setTaskFileCreator(String taskFileCreator) {
        this.taskFileCreator = taskFileCreator;
    }

    public Timestamp getTaskFileDate() {
        return taskFileDate;
    }

    public void setTaskFileDate(Timestamp taskFileDate) {
        this.taskFileDate = taskFileDate;
    }


    @Override
    public TaskFile mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskFile(
                resultSet.getString("taskFileId"),
                resultSet.getString("taskId"),
                resultSet.getString("taskFileName"),
                resultSet.getString("taskFileUrl"),
                resultSet.getString("taskFileCreator"),
                resultSet.getTimestamp("taskFileDate")
        );
    }
}
