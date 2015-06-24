package com.petgang.service.captcha;

import com.petgang.model.captcha.CaptchaBiz;

public class CaptchaServiceImpl implements CaptchaService {

	@Override
	public String getRandomCaptcha(int unqueId, CaptchaBiz biz) {
		return null;
	}

	@Override
	public boolean check(String captcha, String unque, CaptchaBiz biz) {
		return false;
	}

}
