package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserActivityDto  implements RowMapper<UserActivityDto> {
    private String date;
    private int totalActiveMemberCount;
    private int totalTaskCompletionMemberCount;

    public UserActivityDto() {
    }

    public UserActivityDto(String date, int totalActiveMemberCount, int totalTaskCompletionMemberCount) {
        this.date = date;
        this.totalActiveMemberCount = totalActiveMemberCount;
        this.totalTaskCompletionMemberCount = totalTaskCompletionMemberCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalActiveMemberCount() {
        return totalActiveMemberCount;
    }

    public void setTotalActiveMemberCount(int totalActiveMemberCount) {
        this.totalActiveMemberCount = totalActiveMemberCount;
    }

    public int getTotalTaskCompletionMemberCount() {
        return totalTaskCompletionMemberCount;
    }

    public void setTotalTaskCompletionMemberCount(int totalTaskCompletionMemberCount) {
        this.totalTaskCompletionMemberCount = totalTaskCompletionMemberCount;
    }

    @Override
    public UserActivityDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserActivityDto(
                resultSet.getString("date"),
                resultSet.getInt("totalActiveMemberCount"),
                resultSet.getInt("totalTaskCompletionMemberCount")
        );
    }
}
