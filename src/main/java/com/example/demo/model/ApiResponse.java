package com.example.demo.model;

public class ApiResponse {
    private int statusCode;
    private String message;
    private String resultCode;

    public ApiResponse(int statusCode, String message, String resultCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.resultCode = resultCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
