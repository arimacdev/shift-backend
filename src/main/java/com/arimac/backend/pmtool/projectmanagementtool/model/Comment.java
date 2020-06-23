package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Comment implements RowMapper<Comment> {
    private String commentId;
    private String entityId;
    private String content;
    private String commenter;
    private Timestamp commentedAt;
    private boolean isParent;
    private String parentId;
    private boolean isUpdated;
    private boolean isDeleted;

    public Comment() {
    }

    public Comment(String commentId, String entityId, String content, String commenter, Timestamp commentedAt, boolean isParent, String parentId, boolean isUpdated, boolean isDeleted) {
        this.commentId = commentId;
        this.entityId = entityId;
        this.content = content;
        this.commenter = commenter;
        this.commentedAt = commentedAt;
        this.isParent = isParent;
        this.parentId = parentId;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public Timestamp getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Timestamp commentedAt) {
        this.commentedAt = commentedAt;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean parent) {
        isParent = parent;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }


    public boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(boolean updated) {
        isUpdated = updated;
    }

    @Override
    public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Comment(
                resultSet.getString("commentId"),
                resultSet.getString("entityId"),
                resultSet.getString("content"),
                resultSet.getString("commenter"),
                resultSet.getTimestamp("commentedAt"),
                resultSet.getBoolean("isParent"),
                resultSet.getString("parentId"),
                resultSet.getBoolean("isUpdated"),
                resultSet.getBoolean("isDeleted")
        );
    }
}
