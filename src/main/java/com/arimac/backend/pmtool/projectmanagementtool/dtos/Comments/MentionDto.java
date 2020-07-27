package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MentionDto {
    @NotNull
    private String commentId;
    @NotNull
    private String entityId;
    private List<String> recipients;


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

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }
}
