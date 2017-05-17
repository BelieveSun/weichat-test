package com.believe.sun.result;

/**
 * Created by sungj on 17-5-16.
 */
public class DataResult<T> extends BaseResult {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
