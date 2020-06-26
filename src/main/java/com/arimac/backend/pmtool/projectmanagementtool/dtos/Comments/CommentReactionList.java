package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class CommentReactionList {
    private String commentId;
    private String content;
    private String commenter;
    private String commenterFistName;
    private String commenterLatName;
    private String commenterProfileImage;
    private Timestamp commentedAt;
    private List<ReactionRespondants> reactions;

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

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getCommenterFistName() {
        return commenterFistName;
    }

    public void setCommenterFistName(String commenterFistName) {
        this.commenterFistName = commenterFistName;
    }

    public String getCommenterLatName() {
        return commenterLatName;
    }

    public void setCommenterLatName(String commenterLatName) {
        this.commenterLatName = commenterLatName;
    }

    public String getCommenterProfileImage() {
        return commenterProfileImage;
    }

    public void setCommenterProfileImage(String commenterProfileImage) {
        this.commenterProfileImage = commenterProfileImage;
    }

    public Timestamp getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Timestamp commentedAt) {
        this.commentedAt = commentedAt;
    }

    public List<ReactionRespondants> getReactions() {
        return reactions;
    }

    public void setReactions(List<ReactionRespondants> reactions) {
        this.reactions = reactions;
    }
}
