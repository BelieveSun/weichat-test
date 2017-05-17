package com.believe.sun.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tom on 2/24/16.
 */
@RestController
public class ErrorController {
    @RequestMapping(value = "/error")
    public ResponseEntity error(HttpServletResponse response, HttpServletRequest request) {
        int status = response.getStatus();
        return new ResponseEntity(HttpStatus.valueOf(status));
    }
}
