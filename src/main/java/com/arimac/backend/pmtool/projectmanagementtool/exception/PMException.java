package com.arimac.backend.pmtool.projectmanagementtool.exception;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import org.springframework.http.HttpStatus;

public class PMException  extends  RuntimeException{
    private static final long serialVersionUID = 1L;
    private ErrorMessage error;

    public PMException(ErrorMessage error) {
        super(error.getMessage());
        this.error = error;
    }

    public PMException(String message) {
        this(new ErrorMessage(message, HttpStatus.INTERNAL_SERVER_ERROR));
    }

//    public PMException(ResponseMessage responseMessage){
//        this(new ErrorMessage(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR));
//    }

    public PMException(String message, HttpStatus httpStatus){
        this(new ErrorMessage(message, httpStatus));
    }

    public PMException(ResponseMessage message, HttpStatus httpStatus){
        this(new ErrorMessage(message, httpStatus));
    }

    public PMException(Throwable cause){
        this(new ErrorMessage(cause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public ErrorMessage getError(){
        return this.error;
    }
}
