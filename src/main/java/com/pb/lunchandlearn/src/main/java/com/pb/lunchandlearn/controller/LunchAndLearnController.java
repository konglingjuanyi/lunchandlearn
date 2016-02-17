package com.pb.lunchandlearn.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by DE007RA on 08/01/2015.
 */
@Controller("lunchandlearnController")
public class LunchAndLearnController {

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView home() {
		return new ModelAndView("index");
	}
}