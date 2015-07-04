package com.petgang.web.controller.pet;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.petgang.model.pet.Pet;
import com.petgang.service.pet.PetService;
import com.petgang.web.model.response.Code;
import com.petgang.web.model.response.Response;

@Controller
public class PetController {

	@Autowired
	private PetService petService;

	@Resource(name = "jsonView")
	private View jsonView;

	private Log logger = LogFactory.getLog(getClass());

	public ModelAndView addNewPet(
			@RequestParam("name") String name,
			@RequestParam("birth") String birthdayStr,
			@RequestParam("category") long categoryStr,
			@RequestParam(value = "descr", required = false, defaultValue = "") String descr,
			@RequestParam(value = "gender", required = false, defaultValue = "0") int gender) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {
			if (StringUtils.isBlank(name)) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.FAILED, "名字不可为空"));
			}
			if (name.length() > 14 || name.length() < 2) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.FAILED, "名字需要2～14个字符"));
			}
			if (StringUtils.length(descr) > 200) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.FAILED, "描述最多200字符"));
			}
			Date birthday = StringUtils.isBlank(birthdayStr) ? null : DateUtils
					.parseDate(birthdayStr, "yyyy-MM-dd");
			Pet pet = new Pet();
			pet.setName(name);
			pet.setDescr(descr);
			pet.setCategory(categoryStr);
			pet.setGender(gender);
			pet.setPic("");
			pet.setBirthday(birthday);
			pet.setUserId(123);
			petService.add(pet);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			mav.addObject(Response.RESP,
					Response.getInstance(Code.FAILED, "无服务器开小差"));
		}
		return mav;

	}

}
