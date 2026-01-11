package com.learn.EmailNotificationService.exception;

import org.springframework.dao.RecoverableDataAccessException;

public class RetryableException extends RecoverableDataAccessException {

    public RetryableException(String msg){
        super(msg);
    }
}
