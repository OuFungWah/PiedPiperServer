package com.crazywah.bean;

import com.crazywah.common.ResponseStateCode;

public class ResponseBase<T> {

    private int status;
    private String message;
    private T result;

    public ResponseBase() {
    }

    public ResponseBase(int status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        this.message = ResponseStateCode.getMessageByCode(status);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
