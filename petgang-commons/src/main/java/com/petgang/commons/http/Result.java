package com.petgang.commons.http;

public class Result {

    /**
     * HTTP请求状态码
     */
    private int code;

    /**
     * 请求结果
     */
    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
