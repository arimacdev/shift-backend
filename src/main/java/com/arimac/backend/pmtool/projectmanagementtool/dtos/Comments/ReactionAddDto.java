package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

import javax.validation.constraints.NotNull;

public class ReactionAddDto {
    @NotNull
    private String reactionId;

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    @Override
    public String toString() {
        return "ReactionAddDto{" +
                "reactionId='" + reactionId + '\'' +
                '}';
    }
}
