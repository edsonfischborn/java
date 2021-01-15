package com.pg3.util;

import org.springframework.http.HttpStatus;

public class ClientException extends Exception {
    private String message;
    private HttpStatus status;

    public ClientException(String message, HttpStatus status) {
        setMessage(message);
        setStatus(status);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }


}
