package com.byl.springboottest.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;


public class ResponseData implements Serializable {
    private int code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;//如果为空，则不返回

    public ResponseData() {

    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
