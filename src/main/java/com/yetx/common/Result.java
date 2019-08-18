package com.yetx.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(ResultStatus status,T data) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
    }
    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        data = null;
    }


    public static  <T> Result<T> success(T data){
        return new Result<T>(ResultStatus.SUCCESS,data);
    }
    public static  <T> Result<T> error(ResultStatus status){
        return new Result<T>(status.getCode(),status.getMsg());
    }
}
