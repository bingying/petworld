package com.petgang.web.controller.user;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.petgang.model.user.User;
import com.petgang.service.user.UserService;
import com.petgang.web.model.response.Code;
import com.petgang.web.model.response.Response;
import com.petgang.web.model.user.RegisterUserView;

/**
 * 注册
 * 
 * @author bingying
 * 
 */
@Controller
public class RegisterController {

	@Resource(name = "jsonView")
	private View jsonView;

	@Autowired
	private UserService userService;

	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/v1/register", method = RequestMethod.POST)
	public ModelAndView register(@RequestParam("tel") String tel,
			@RequestParam("captcha") String captcha,
			@RequestParam("pw") String password) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {
			int userId = userService.register(tel, password);
			if (userId <= 0) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.FAILED, "注册失败", ""));
			}
			User user = userService.get(userId);
			RegisterUserView view = RegisterUserView.form(user);
			mav.addObject(Response.RESP,
					Response.getInstance(Code.SUCCESS, "", view));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			mav.addObject(Response.RESP,
					Response.getInstance(Code.FAILED, "服务器开小差", ""));
		}

		return mav;
	}

	@RequestMapping(value = "/v1/register/captcha", method = RequestMethod.POST)
	public ModelAndView getCaptcha(@RequestParam("tel") String tel) {
		ModelAndView mav = new ModelAndView(jsonView);

		return mav;
	}
}
