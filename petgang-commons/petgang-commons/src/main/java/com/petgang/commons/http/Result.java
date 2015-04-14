package com.petgang.commons.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.methods.CloseableHttpResponse;

public class Result {

    private int code;

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

    public static Result getInstance(CloseableHttpResponse response) throws IllegalStateException,
            IOException {
        Result result = new Result();
        InputStream in = response.getEntity().getContent();
        result.setCode(response.getStatusLine().getStatusCode());
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        result.setBody(new String(bytes));
        return result;
    }

}
