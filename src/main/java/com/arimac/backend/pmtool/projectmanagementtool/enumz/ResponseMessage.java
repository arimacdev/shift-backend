package com.arimac.backend.pmtool.projectmanagementtool.enumz;

public enum ResponseMessage {
    SUCCESS("success"),
    NO_RECORD("No Record");

    private String message;
    private ResponseMessage(String message){
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
