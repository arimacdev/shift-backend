package com.arimac.backend.pmtool.projectmanagementtool.exception;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
    private String message;
    private String title;
    private HttpStatus code;
    private  int status;
    private String timestamp;
    private Object data;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }

    public ErrorMessage(String message, String title, HttpStatus code, int status, String timestamp, Object data) {
        this.message = message;
        this.title = title;
        this.code = code;
        this.status = status;
        this.timestamp = timestamp;
        this.data = data;
    }

    public HttpStatus getError(){
        return this.code;
    }
}
