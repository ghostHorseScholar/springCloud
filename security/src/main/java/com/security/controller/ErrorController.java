package com.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@RequestMapping("error")
public class ErrorController {

	@GetMapping
	public String error(String strJson) {
		return strJson;
	}
}
