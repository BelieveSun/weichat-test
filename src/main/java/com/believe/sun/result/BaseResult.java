package com.believe.sun.result;

/**
 * Created by sungj on 17-5-16.
 */
public class BaseResult {



    private Integer error;

    private String message;

    private String comment;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
