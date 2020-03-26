package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum  LogOperationEnum {
    CREATE("CREATE",1),
    UPDATE("UPDATE",2),
    DELETE("DELETE",3),
    ASSIGN("ASSIGN",4);

    private int operationId;
    private String operation;

    LogOperationEnum(String operation, int operationId){
        this.operation = operation;
        this.operationId = operationId;
    }

    public int getOperationId() {
        return operationId;
    }

    public String getOperation() {
        return operation;
    }

}