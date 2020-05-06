package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum ResponseMessage {
    SUCCESS("success"),
    NO_RECORD("No Record"),
    PROJECT_NOT_FOUND("Project Not Found"),
    NO_ACCESS("Records cannot be accessed"),
    ALREADY_ASSIGNED("Already Assigned"),
    UNAUTHORIZED("Insufficient privileges"),
    INVALID_REQUEST_BODY("Invalid Request Body"),
    ASSIGNER_NOT_MEMBER("Assigner doesn't belong to the project"),
    ASSIGNEE_NOT_MEMBER("Assignee doesn't belong to the project"),
    USER_NOT_MEMBER("User doesn't belong to the project"),
    USER_NOT_GROUP_MEMBER("User doesn't belong to this Group"),
    USER_NOT_FOUND("User doesn't exist"),
    UNAUTHORIZED_OPERATION("This Operation is not permitted!"),
    TASK_NOT_CHILD_TASK("This Task is not a child task"),
    TASK_NOT_PARENT_TASK("This Task is not a Parent task");



    private String message;
    private ResponseMessage(String message){
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
