package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

import java.util.List;

public class ReactionRespondants {
    private String reactionId;
   // private int responses;
    private List<UserReaction> respondants;


//    public int getResponses() {
//        return responses;
//    }
//
//    public void setResponses(int responses) {
//        this.responses = responses;
//    }

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public List<UserReaction> getRespondants() {
        return respondants;
    }

    public void setRespondants(List<UserReaction> respondants) {
        this.respondants = respondants;
    }
}
