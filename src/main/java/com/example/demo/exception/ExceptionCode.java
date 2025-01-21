package com.example.demo.exception;

public enum ExceptionCode {
    UNABLE_TO_SEND_EMAIL("E001", "Unable to send email."),
    NO_SUCH_ALGORITHM("E002", "No such algorithm available."),
    INVALID_PARAMETER("E003", "Invalid parameter provided."),
    DUPLICATE_EMAIL("E004", "Duplicate email detected."),
    UNAUTHORIZED_ACCESS("E005", "Unauthorized access."),
    RESOURCE_NOT_FOUND("E006", "Requested resource not found."),
    INTERNAL_SERVER_ERROR("E007", "Internal server error."),
    MEMBER_EXISTS("E007", "A member with this email already exists.");

    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
