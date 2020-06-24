package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum ResponseMessage {
    SUCCESS("success"),
    PROJECT_ALIAS_EXIST("Project Alias Exists"),
    NO_RECORD("No Record"),
    SPRINT_NOT_FOUND("Sprint Not Found"),
    TASK_NOT_FOUND("Task Not Found"),
    PROJECT_NOT_FOUND("Project Not Found"),
    NO_ACCESS("Records cannot be accessed"),
    INVALID_DATE_FORMAT("Invalid Date Format"),
    ALREADY_ASSIGNED("Already Assigned"),
    USERNAME_EXISTS("Username Already Exists"),
    ADMIN_USER_NOT_FOUND("Admin User Not Found"),
    UNAUTHORIZED("Insufficient privileges"),
    INVALID_FILTER_QUERY("Invalid Filter Query"),
    URL_DECODING_ERROR("Error Decoding URL Query String"),
    INVALID_REQUEST_BODY("Invalid Request Body"),
    ASSIGNER_NOT_MEMBER("Assigner doesn't belong to the project"),
    ASSIGNEE_NOT_MEMBER("Assignee doesn't belong to the project"),
    USER_NOT_MEMBER("User doesn't belong to the project"),
    USER_NOT_GROUP_MEMBER("User doesn't belong to this Group"),
    USER_NOT_FOUND("User doesn't exist"),
    ALREADY_DEACTIVATED("User Already Deactivated"),
    ALREADY_ACTIVATED("User Already Activated"),
    UNAUTHORIZED_OPERATION("This Operation is not permitted!"),
    TASK_NOT_CHILD_TASK("This Task is not a child task"),
    TASK_NOT_PARENT_TASK("This Task is not a Parent task"),
    CANNOT_TRANSITION_CHILD_TASK("Cannot Transition a Child Task"),
    PARENT_TASK_NOT_FOUND("Parent Task Not Found"),
    PARENT_TASK_HAS_CHILDREN("Parent Task Has Children"),
    PARENT_TASK_HAS_PENDING_CHILD_TASKS("Parent Task Has Open Children"),
    CATEGORY_NAME_EXIST("Category Exist By Category Name"),
    CATEGORY_NOT_FOUND("Category Not Found"),
    SKILL_NAME_EXIST("Skill Exist By Skill Name"),
    SKILL_NOT_FOUND("Skill Not Found"),
    ASSIGNER_NOT_FOUND("Assigner Not Found"),
    ASSIGNEE_NOT_FOUND("Assignee Not Found"),
    SKILL_ALREADY_ADDED("Skill Already Added to User"),
    SKILL_NOT_ADDED("Skill Not Added to User"),
    NOT_REGISTERED_FOR_PROVIDER("User Not Registered for Provider"),
    ALREADY_UNSUBSCRIBED("User Already Unsubscribed!"),
    ALREADY_SUBSCRIBED("User Already Subscribed!"),
    PARENT_COMMENT_NOT_FOUND("Parent Comment Not Found"),
    COMMENT_NOT_FOUND("Comment Not Found");


    private String message;
    ResponseMessage(String message){
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
