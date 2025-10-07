package com.project.bff_scheduler.infrastructure.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }

}
