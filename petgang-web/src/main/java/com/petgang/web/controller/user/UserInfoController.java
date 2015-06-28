package com.petgang.web.controller.user;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.petgang.model.user.Gender;
import com.petgang.service.user.UserService;
import com.petgang.web.model.response.Code;
import com.petgang.web.model.response.Response;

/**
 * 用户信息更新
 * 
 * @author bingying
 * 
 */
@Controller
public class UserInfoController {

	@Resource(name = "jsonView")
	private View jsonView;

	@Autowired
	private UserService userService;

	private static final Log logger = LogFactory
			.getLog(UserInfoController.class);

	/**
	 * 更新用户昵称
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/v1/user/update_n", method = RequestMethod.POST)
	public ModelAndView updateName(@RequestParam("name") String nickName) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {
			if (userService.updateNickName(0, nickName) > 0) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.SUCCESS, "修改成功"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return mav.addObject(Response.RESP,
				Response.getInstance(Code.FAILED, "修改失败"));
	}

	@RequestMapping(value = "/v1/user/update_f", method = RequestMethod.POST)
	public ModelAndView updateFacePicture(Collection<MultipartFile> files) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return mav;
	}
	@RequestMapping(value = "/v1/user/update_g", method = RequestMethod.POST)
	public ModelAndView updateGender(@RequestParam("g") int gender) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {
			Gender genderE = Gender.getGender(gender);
			if (genderE == null) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.FAILED, "请选择性别"));
			}
			if (userService.updateGender(0, genderE) > 0) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.SUCCESS, "修改成功"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return mav.addObject(Response.RESP,
				Response.getInstance(Code.FAILED, "修改失败"));
	}
	@RequestMapping(value = "/v1/user/update_addr", method = RequestMethod.GET)
	public ModelAndView updateAddress(@RequestParam("city") int city,
			@RequestParam("addr") String addr) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return mav;
	}
}
