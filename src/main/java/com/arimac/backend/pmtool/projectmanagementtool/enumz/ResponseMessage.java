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
    TASK_NOT_PARENT_TASK("This Task is not a Parent task"),
    CANNNOT_TRANSITION_CHILD_TASK("Cannot Transition a Child Task"),
    PARENT_TASK_NOT_FOUND("Parent Task Not Found"),
    PARENT_TASK_HAS_CHILDREN("Parent Task Has Children"),
    PARENT_TASK_HAS_PENDING_CHILD_TASKS("Parent Task Has Pending Children")
    ;



    private String message;
    private ResponseMessage(String message){
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
