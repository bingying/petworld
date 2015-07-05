package com.petgang.web.model.response;

public enum Code implements BaseCode {
	SUCCESS(0, "成功"), //
	FAILED(1000, "服务器开小差"), //
	C304(304, "资源无需更新");

	private int code;
	private String desc = "";
	private Code(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public int getCode() {
		return this.code;
	}
	@Override
	public String getDesc() {

		return desc;
	}
}
