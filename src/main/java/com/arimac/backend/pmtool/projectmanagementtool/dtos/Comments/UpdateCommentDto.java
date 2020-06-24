package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

import javax.validation.constraints.NotEmpty;

public class UpdateCommentDto {
    @NotEmpty
    private String content;
    @NotEmpty
    private String commenter;

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
}
