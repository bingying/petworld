package com.petgang.web.model.response;

public enum Code implements BaseCode {
	SUCCESS(0), //
	FAILED(1000);
	private int code;

	private Code(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return this.code;
	}
}
