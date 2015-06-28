package com.petgang.web.model.response;

public class Response {

	private int code;

	private String msg = "";

	private Object data;

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

	public static <T extends BaseCode> Response getInstance(T code, String msg) {
		Response response = new Response();
		response.setCode(code.getCode());
		response.setMsg(msg);
		return response;
	}

	public static <T extends BaseCode> Response getInstance(T code, String msg,
			Object data) {
		Response response = new Response();
		response.setCode(code.getCode());
		response.setData(data);
		response.setMsg(msg);
		return response;
	}
	public static <T extends BaseCode> Response getInstance(T code) {
		Response response = new Response();
		response.setCode(code.getCode());
		response.setMsg(code.getDesc());
		return response;

	}

	public static final String RESP = "RESP";
}
