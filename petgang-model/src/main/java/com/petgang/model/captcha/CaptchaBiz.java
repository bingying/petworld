package com.petgang.model.captcha;

public enum CaptchaBiz {

	Register("register");

	private String biz;

	private CaptchaBiz(String biz) {
		this.biz = biz;
	}

	public String getBiz() {
		return this.biz;
	}

}
