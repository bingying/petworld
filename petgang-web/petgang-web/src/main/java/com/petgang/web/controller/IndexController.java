package com.petgang.web.controller;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.petgang.service.user.UserService;

@Controller
public class IndexController {

    @Autowired
    private Properties configProperties;

    @Resource(name = "jsonView")
    private View jsonView;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView(jsonView);
        return mav.addObject(userService.get());
    }

}
