package com.petgang.web.controller;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.google.common.collect.Maps;
import com.petgang.web.model.response.Code;
import com.petgang.web.model.response.Response;
/**
 * 文件上传
 * 
 * @author bingliu
 *
 */
@Controller
public class FileUploadController {

	@Resource(name = "jsonView")
	private View jsonView;

	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/v1/up/img", method = RequestMethod.POST)
	public ModelAndView uploadImage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(jsonView);
		try {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> upFileMap = multipartHttpServletRequest
					.getFileMap();
			if (MapUtils.isEmpty(upFileMap)) {
				return mav.addObject(Response.RESP,
						Response.getInstance(Code.FAILED, "请上传文件"));
			}
			Map<String, byte[]> fileMap = Maps.newHashMap();
			for (Entry<String, MultipartFile> tmp : upFileMap.entrySet()) {
				fileMap.put(tmp.getKey(), tmp.getValue().getBytes());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			mav.addObject(Response.RESP, Response.getInstance(Code.FAILED));
		}
		return mav;
	}
}
