package com.example.demo.model;

public class EmailVerificationResult {
    private final boolean success;

    private EmailVerificationResult(boolean success) {
        this.success = success;
    }

    public static EmailVerificationResult of(boolean success) {
        return new EmailVerificationResult(success);
    }

    public boolean isSuccess() {
        return success;
    }
}