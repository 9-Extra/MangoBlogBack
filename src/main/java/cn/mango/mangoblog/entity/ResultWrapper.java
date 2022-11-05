package cn.mango.mangoblog.entity;

import lombok.Data;

@Data
public class ResultWrapper<T> {
    private long code;
    private String message;
    private T data;

    public ResultWrapper(long code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultWrapper(T data){
        this.code = 200;
        this.message = "Success";
        this.data = data;
    }
}
