package com.thymeleaf.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Utils {

	public static boolean isEmpty(String arg) {
		if (arg == null || "".equals(arg)) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object arg) {
		if (arg == null || "".equals(arg)) {
			return true;
		}
		return isEmpty(arg.toString());
	}
	
	public static JSONObject getJsonObject(ResponseEntity<String> entity) {
		if (entity == null) {
			return null;
		}
		JSONObject jsonObject = JSONObject.parseObject(entity.getBody());
		if (jsonObject == null) {
			return null;
		}
		if (AttributeConfig.SUCCESS.equals(jsonObject.get("status"))) {
			if (entity.getBody().indexOf("status") > 0 && entity.getBody().indexOf("status",entity.getBody().indexOf("status")+6) < 0) {
				return jsonObject;
			}
			return JSONObject.parseObject(jsonObject.getString("msg"));
		} else {
			return jsonObject;
		}
	}
	
	public static String getBody(HttpServletRequest request, ResponseEntity<String> entity) {
		return getBody(request, entity, null, null);
	}
	

	public static String getBody(HttpServletRequest request, ResponseEntity<String> entity, String url) {
		return getBody(request, entity, null, url);
	}
	
	public static String getBody(HttpServletRequest request, ResponseEntity<String> entity, Model model, String url) {
		
		JSONObject jsonObject = Utils.getJsonObject(entity);
		
		if (jsonObject == null) {
			return "";
		}
		
		switch (jsonObject.get("status").toString()) {
		case AttributeConfig.SUCCESS:
			if (entity.getHeaders().get(AttributeConfig.TOKEN_NAME) != null) {
				request.getSession().setAttribute(AttributeConfig.TOKEN_NAME, entity.getHeaders().get(AttributeConfig.TOKEN_NAME).get(0));
			}
			break;
		case AttributeConfig.LOGIN_OUT_TIME:
		case AttributeConfig.LOGIN_FAIL:
		case AttributeConfig.LOGIN_NOT:
			request.getSession().setAttribute(AttributeConfig.TOKEN_NAME, null);
			return "redirect:/";
		case AttributeConfig.PERMISSION_NOT:
		case AttributeConfig.FAIL:
			if ("No token was found".equals(jsonObject.get("msg").toString())) {
				return "redirect:/";
			}
			model.addAttribute("datas", null);
			return url;
		}
		
		Object obj = jsonObject.get("data");
		if (model != null && obj != null) {
			if (obj instanceof JSONObject) {
				model.addAttribute("pageInfo", jsonObject.getJSONObject("data"));
				model.addAttribute("datas", jsonObject.getJSONObject("data").getJSONArray("list"));
			} else if (obj instanceof JSONArray) {
				model.addAttribute("datas", jsonObject.getJSONArray("data"));
			}
		}
		if (isEmpty(url)) {
			return jsonObject.toJSONString();
		}
		return url;
	}
}
