package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserNumberDto implements RowMapper<UserNumberDto> {
    private int totalUsers;
    private int slackActivated;

    public UserNumberDto() {
    }

    public UserNumberDto(int totalUsers, int slackActivated) {
        this.totalUsers = totalUsers;
        this.slackActivated = slackActivated;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getSlackActivated() {
        return slackActivated;
    }

    public void setSlackActivated(int slackActivated) {
        this.slackActivated = slackActivated;
    }

    @Override
    public UserNumberDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserNumberDto(
                resultSet.getInt("totalUsers"),
                resultSet.getInt("slackActivated")
        );
    }
}
