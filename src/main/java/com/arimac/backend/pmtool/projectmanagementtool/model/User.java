package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements RowMapper<User> {
    private String userId;
    private String idpUserId;
    private String firstName;
    private String lastName;
    private String email;

    public User() {
    }

    public User(String userId, String idpUserId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.idpUserId = idpUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getIdpUserId() {
        return idpUserId;
    }

    public void setIdpUserId(String idpUserId) {
        this.idpUserId = idpUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(
                resultSet.getString("userId"),
                resultSet.getString("idpUserId"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("email")
        );
    }
}
