package com.believe.sun.result;

/**
 * Created by sungj on 17-5-16.
 */
public enum ErrorCode {


    SUCCESS(0,"success"),
    STATE_NOT_MATCH(70001,"state not match"),
    SERVER_INNER_ERROR(70002,"server inner error");


    private Integer code;
    private String message;
    private String comment;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    ErrorCode(Integer code, String message, String comment) {
        this.code = code;
        this.message = message;
        this.comment = comment;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return getComment() != null ? getComment() : this.message;
    }

    public String getComment() {
        return this.comment;
    }

}
