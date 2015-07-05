package com.petgang.web.controller.pet;

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

import com.petgang.commons.JsonUtil;
import com.petgang.commons.security.MD5;
import com.petgang.service.pet.PetCategoryService;
import com.petgang.web.model.pet.PetCategoryView;
import com.petgang.web.model.response.Code;
import com.petgang.web.model.response.Response;

@Controller
public class PetCategoryController {

	@Autowired
	private PetCategoryService petCategoryService;

	@Resource(name = "jsonView")
	private View jsonView;

	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/v1/pet/categories", method = RequestMethod.GET)
	public ModelAndView getCategories(@RequestParam("sign") String sign) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {
			if(sign.equals(sign)){
				return mav.addObject(Response.RESP, Response.getInstance(Code.C304, ""));
			}
			PetCategoryView categoryView = new PetCategoryView();
			categoryView.setGroups(petCategoryService.getCategoryGroupMap());
			categoryView.setNames(petCategoryService.getCategoryMap());
			Response response = Response.getInstance(Code.SUCCESS, "",
					categoryView);
			mav.addObject(Response.RESP, response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			mav.addObject(Response.RESP,
					Response.getInstance(Code.FAILED, "服务器开小差"));
		}
		return mav;
	}

	String sign() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				JsonUtil.convert(petCategoryService.getCategoryGroupMap()))
				.append(petCategoryService.getCategoryMap());
		return MD5.encode(builder.toString());
	}
}
