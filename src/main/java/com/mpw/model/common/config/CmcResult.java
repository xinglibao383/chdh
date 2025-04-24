package com.mpw.model.common.config;


import com.mpw.model.common.enums.ReturnEnum;

import java.io.Serializable;
import java.text.MessageFormat;

public class CmcResult<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    private int code;
    private String message;
    private T data;

    public CmcResult() {}

    public CmcResult(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
    public CmcResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CmcResult(ReturnEnum statusEnum, String...strings){
        this.code = statusEnum.getCode();
        this.message = MessageFormat.format(statusEnum.getMessage(),strings);
    }


    public static <T> CmcResult<T> success(T data){
        return new CmcResult<T>(200, "success", data);
    }

    public static <T> CmcResult<T> success() {
        return (CmcResult<T>) success("");
    }

    public static <T> CmcResult<T> error(String msg){
        return new CmcResult<T>(-1, msg);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
