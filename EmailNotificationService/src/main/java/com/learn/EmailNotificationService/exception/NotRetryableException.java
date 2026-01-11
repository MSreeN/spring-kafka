package com.learn.EmailNotificationService.exception;

public class NotRetryableException extends  RuntimeException {
    public NotRetryableException(String msg){
        super(msg);
    }
}
