package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum ResponseMessage {
    SUCCESS("success"),
    NO_RECORD("No Record"),
    NO_ACCESS("Records cannot be accessed"),
    ALREADY_ASSIGNED("Already Assigned"),
    UNAUTHORIZED("Insufficient privileges"),
    INVALID_REQUEST_BODY("Invalid Request Body"),
    ASSIGNER_NOT_MEMBER("Assigner doesn't belong to the project"),
    ASSIGNEE_NOT_MEMBER("Assignee doesn't belong to the project"),
    USER_NOT_MEMBER("User doesn't belong to the project"),
    USER_NOT_FOUND("User doesn't exist"),
    UNAUTHORIZED_OPERATION("This Operation is not permitted!");


    private String message;
    private ResponseMessage(String message){
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
