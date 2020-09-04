package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DateCountDto implements RowMapper<DateCountDto> {
    private String date;
    private int taskCount;

    public DateCountDto() {
    }

    public DateCountDto(String date, int taskCount) {
        this.date = date;
        this.taskCount = taskCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    @Override
    public DateCountDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new DateCountDto(
                resultSet.getString("date"),
                resultSet.getInt("taskCount")
        );
    }
}
