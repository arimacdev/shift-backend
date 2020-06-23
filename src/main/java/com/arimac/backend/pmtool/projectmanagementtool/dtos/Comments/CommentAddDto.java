package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

import javax.validation.constraints.NotEmpty;

public class CommentAddDto {
    @NotEmpty
    private String entityId;
    @NotEmpty
    private String content;
    @NotEmpty
    private String commenter;
    private String parentId;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CommentAddDto{" +
                "entityId='" + entityId + '\'' +
                ", content='" + content + '\'' +
                ", commenter='" + commenter + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
