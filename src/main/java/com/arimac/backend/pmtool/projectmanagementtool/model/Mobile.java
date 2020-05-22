package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mobile implements RowMapper<Mobile> {
    private String platform;
    private int latest_version;
    private int current_version;
    private boolean force_update;

    public Mobile() {
    }

    public Mobile(String platform, int latest_version, int current_version, boolean force_update) {
        this.platform = platform;
        this.latest_version = latest_version;
        this.current_version = current_version;
        this.force_update = force_update;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getLatest_version() {
        return latest_version;
    }

    public void setLatest_version(int latest_version) {
        this.latest_version = latest_version;
    }

    public int getCurrent_version() {
        return current_version;
    }

    public void setCurrent_version(int current_version) {
        this.current_version = current_version;
    }

    public boolean isForce_update() {
        return force_update;
    }

    public void setForce_update(boolean force_update) {
        this.force_update = force_update;
    }

    @Override
    public Mobile mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Mobile(
                resultSet.getString("platform"),
                resultSet.getInt("latest_version"),
                resultSet.getInt("current_version"),
                resultSet.getBoolean("force_update")
        ) ;
    }
}
