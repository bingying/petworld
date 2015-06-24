package com.petgang.service.captcha;

import com.petgang.model.captcha.CaptchaBiz;

public interface CaptchaService {

	String getRandomCaptcha(int unqueId,CaptchaBiz biz);

	boolean check(String captcha,String unque,CaptchaBiz biz);
}
