package com.zking.entity;

import lombok.Data;

@Data
public class Result<T> {
    private Boolean boo;
    private String info;
    private T data;

    //成功
    public static Result success(String reason){
        Result<String> result = new Result<>();
        result.setBoo(true);
        result.setInfo(reason);
        return result;
    }

    //失败
    public static Result fail(String reason){
        Result<String> result = new Result<>();
        result.setBoo(false);
        result.setInfo(reason);
        return result;
    }
}
