package com.arimac.backend.pmtool.projectmanagementtool.Response;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import org.springframework.http.HttpStatus;

public class Response {
    private String message;
    private Object data;
    private int status;

    public Response() {
        this.status = HttpStatus.OK.value();
    }

    public Response(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
    }

    public Response(ResponseMessage message) {
        this(message.toString());
    }

    public Response(ResponseMessage message, Object data) {
        this(message.toString(), data);
    }

    public Response(ResponseMessage message, String requestId) {
        this(message.toString());
    }

    public Response(String message, Object data) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
