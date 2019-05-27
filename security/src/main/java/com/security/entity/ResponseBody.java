package com.security.entity;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.Data;

@Data
public class ResponseBody {
	
	@JSONField(ordinal=1)
	private String status;
	
	@JSONField(ordinal=2)
	private String msg;
	
	@JSONField(ordinal=3)
	private Object data;
	
	public ResponseBody() {}
	
	public ResponseBody(String status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	
	public ResponseBody(String status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	public String toString(HttpServletRequest request) {
		String json = toString();
		System.out.println("json==>"+json);
		String callback = request.getParameter("callback"); 
    	if (callback == null || callback == "") {
			return json;
		}
		return callback + "(" + json + ")";
	}
	
	public String toString() {
		return JSON.toJSONString(this,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteNullBooleanAsFalse);
	}
	
}
