package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class CommentReaction implements RowMapper<CommentReaction> {
    private String commentId;
    private String content;
    private Timestamp commentedAt;
    private boolean isUpdated;

    private String reactionId;
    private Timestamp reactedAt;

    private String commenterId;
    private String commenterFirstName;
    private String commenterLastName;
    private String commenterProfileImage;

    private String responderId;
    private String responderFirstName;
    private String responderLastName;
    private String responderProfileImage;


    public CommentReaction() {
    }

    public CommentReaction(String commentId, String content, Timestamp commentedAt, boolean isUpdated, String reactionId, Timestamp reactedAt, String commenterId, String commenterFirstName, String commenterLastName, String commenterProfileImage, String responderId, String responderFirstName, String responderLastName, String responderProfileImage) {
        this.commentId = commentId;
        this.content = content;
        this.commentedAt = commentedAt;
        this.isUpdated = isUpdated;
        this.reactionId = reactionId;
        this.reactedAt = reactedAt;
        this.commenterId = commenterId;
        this.commenterFirstName = commenterFirstName;
        this.commenterLastName = commenterLastName;
        this.commenterProfileImage = commenterProfileImage;
        this.responderId = responderId;
        this.responderFirstName = responderFirstName;
        this.responderLastName = responderLastName;
        this.responderProfileImage = responderProfileImage;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Timestamp commentedAt) {
        this.commentedAt = commentedAt;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public Timestamp getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(Timestamp reactedAt) {
        this.reactedAt = reactedAt;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterFirstName() {
        return commenterFirstName;
    }

    public void setCommenterFirstName(String commenterFirstName) {
        this.commenterFirstName = commenterFirstName;
    }

    public String getCommenterLastName() {
        return commenterLastName;
    }

    public void setCommenterLastName(String commenterLastName) {
        this.commenterLastName = commenterLastName;
    }

    public String getCommenterProfileImage() {
        return commenterProfileImage;
    }

    public void setCommenterProfileImage(String commenterProfileImage) {
        this.commenterProfileImage = commenterProfileImage;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public String getResponderFirstName() {
        return responderFirstName;
    }

    public void setResponderFirstName(String responderFirstName) {
        this.responderFirstName = responderFirstName;
    }

    public String getResponderLastName() {
        return responderLastName;
    }

    public void setResponderLastName(String responderLastName) {
        this.responderLastName = responderLastName;
    }

    public String getResponderProfileImage() {
        return responderProfileImage;
    }

    public void setResponderProfileImage(String responderProfileImage) {
        this.responderProfileImage = responderProfileImage;
    }

    private Timestamp formatDate(Timestamp commented) {
        commented.setTime(commented.getTime() + TimeUnit.MINUTES.toMillis(330));

        return commented;
    }

    @Override
    public CommentReaction mapRow(ResultSet resultSet, int i) throws SQLException {
        return new CommentReaction(
                resultSet.getString("commentId"),
                resultSet.getString("content"),
                formatDate(resultSet.getTimestamp("commentedAt")),
                resultSet.getBoolean("isUpdated"),
                resultSet.getString("reactionId"),
                resultSet.getTimestamp("reactedAt"),
                resultSet.getString("UC.userId"),
                resultSet.getString("UC.firstName"),
                resultSet.getString("UC.lastName"),
                resultSet.getString("UC.profileImage"),
                resultSet.getString("UR.userId"),
                resultSet.getString("UR.firstName"),
                resultSet.getString("UR.lastName"),
                resultSet.getString("UR.profileImage")

        );
    }
}

