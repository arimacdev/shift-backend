package com.arimac.backend.pmtool.projectmanagementtool.Response;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseController {
    public ResponseController() {
    }

    protected ResponseEntity<Object> sendResponse(Object object) {
        if (object instanceof ErrorMessage) {
            ErrorMessage err = (ErrorMessage)object;
            return this.sendResponse(object, err.getError());
        } else {
            return this.sendResponse(object, HttpStatus.OK);
        }
    }

    private ResponseEntity<Object> sendResponse(Object object, HttpStatus httpStatus) {
        return ((ResponseEntity.BodyBuilder)ResponseEntity.status(httpStatus).header("Content-Type", new String[]{"application/json"})).body(object);
    }
}
