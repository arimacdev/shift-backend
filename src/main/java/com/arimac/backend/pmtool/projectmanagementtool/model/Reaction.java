package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Reaction implements RowMapper<Reaction> {
    private String reactionId;
    private String commentId;
    private String reactorId;
    private Timestamp reactedAt;

    public Reaction(String reactionId, String commentId, String reactorId, Timestamp reactedAt) {
        this.reactionId = reactionId;
        this.commentId = commentId;
        this.reactorId = reactorId;
        this.reactedAt = reactedAt;
    }

    public Reaction() {
    }

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReactorId() {
        return reactorId;
    }

    public void setReactorId(String reactorId) {
        this.reactorId = reactorId;
    }

    public Timestamp getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(Timestamp reactedAt) {
        this.reactedAt = reactedAt;
    }

    @Override
    public Reaction mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Reaction(
                resultSet.getString("reactionId"),
                resultSet.getString("commentId"),
                resultSet.getString("reactorId"),
                resultSet.getTimestamp("reactedAt"));
    }
}
