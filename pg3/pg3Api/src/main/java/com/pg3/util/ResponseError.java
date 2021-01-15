package com.pg3.util;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ResponseError {
    private String message;
    private HttpStatus error;
    private String timestamp;
    private String status;

    public ResponseError(String message, HttpStatus error) {
        this.message = message;
        this.error = error;
        this.status = error.toString().split(" ")[0];
        this.timestamp = new Date(System.currentTimeMillis()).toString();
    }

    public String getMessage() { return message; }

    public HttpStatus getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
