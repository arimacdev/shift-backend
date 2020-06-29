package com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments;

public class UserReaction {
    private String reactionId;
    private String responderId;
    private String responderFirstName;
    private String responderLastName;
    private String responderProfileImage;

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
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
}
