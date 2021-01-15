package com.pg3.util;

public class FileResponse {
    private String base64;

    public FileResponse(String base64){
        setBase64(base64);
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
