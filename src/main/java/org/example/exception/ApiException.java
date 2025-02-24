package org.example.exception;

public class ApiException extends RuntimeException{

    private final int statusCode;
    private final String errorMessage;

    public ApiException(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
