package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Client implements RowMapper<Client> {
    private String clientId;
    private String clientName;
    private String country;

    public Client() {
    }

    public Client(String clientId, String clientName, String country) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.country = country;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public Client mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Client(
                resultSet.getString("clientId"),
                resultSet.getString("clientName"),
                resultSet.getString("country")
        );
    }
}
