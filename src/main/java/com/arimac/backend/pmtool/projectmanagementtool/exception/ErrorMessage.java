package com.arimac.backend.pmtool.projectmanagementtool.exception;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import org.springframework.http.HttpStatus;

import java.util.Date;


public class ErrorMessage {
    private String message;
    private String title;
    private HttpStatus error;
    private int status;
    private String timestamp;
    private Object data;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, HttpStatus error) {
        this.message = message;
        this.error = error;
    }

    public ErrorMessage(ResponseMessage responseMessage, HttpStatus error) {
        this(responseMessage.toString(), error);
    }

     public ErrorMessage(String message, String title, Integer statusCode,  String timestamp) {
        this.message = message;
        this.title = title;
        this.status = statusCode;
        this.error = HttpStatus.valueOf(statusCode);
        this.timestamp = timestamp;
    }

    public ErrorMessage(String message, HttpStatus error, Object data) {
        this.message = message;
        this.error = error;
        this.data = data;
    }


    public ErrorMessage(ResponseMessage message,HttpStatus error, Object data){
        this(message.toString(), error, data);
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HttpStatus getError() {
        return this.error;
    }

    public void setError(String error) {
        if (this.status == 0) {
            this.status = 500;
        }

        this.error = HttpStatus.valueOf(this.status);
    }

    public int getStatus() {
        return this.error.value();
    }

    public void setStatus(int status) {
        this.error = HttpStatus.valueOf(status);
        this.status = status;
    }

    public String getTimestamp() {
        return (new Date()).toString();
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
