package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

        @GetMapping("/health")
        public ResponseEntity<Object> checkHealth(){
            return new ResponseEntity<Object>("Success", HttpStatus.OK);
        }
}
