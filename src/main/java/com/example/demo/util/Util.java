package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	public static String getNowDateStr() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		return format1.format(time);
	}
	
	public static Map<String, Object> mapOf(Object... args){
		if(args.length % 2 != 0) {
			throw new IllegalArgumentException("인자를 짝수개로 입력해주세요.");
		}
		
		int size = args.length / 2;
		
		Map<String, Object> map = new LinkedHashMap<>();
		
		for(int i = 0; i < size; i++) {
			int keyIndex = i * 2;
			int valueIndex = keyIndex + 1;
			
			String key;
			Object value;
			
			try {
				key = (String) args[keyIndex];
			} catch(ClassCastException e) {
				throw new IllegalArgumentException("키는 String 형식으로 입력해야 합니다." + e.getMessage()); 
			}
			
			value = args[valueIndex];
			
			map.put(key, value);
		}
		
		return map;		
	}

	public static int getAsInt(Object object, int defaultValue) {
		if (object instanceof BigInteger) {
			return ((BigInteger) object).intValue();
		} else if (object instanceof Double) {
			return (int) Math.floor((double) object);
		} else if (object instanceof Float) {
			return (int) Math.floor((float) object);
		} else if (object instanceof Long) {
			return (int) object;
		} else if (object instanceof Integer) {
			return (int) object;
		} else if (object instanceof String) {
			return Integer.parseInt((String) object);
		}

		return defaultValue;
	}

	public static String msgAndBack(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("history.back();");
		sb.append("</script>");
		
		return sb.toString();
	}

	public static String msgAndReplace(String msg, String url) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("location.replace('" + url + "')");
		sb.append("</script>");
		
		return sb.toString();
	}

	public static Map<String, Object> getParamMap(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<>();
		
		// request가 갖고 있는 파라메터의 이름들을 저장
		Enumeration<String> parameterNames = request.getParameterNames();
		
		// request에 파라메터가 있을 경우에 파라메터의 수 만큼 반복
		// param에 현재 파라미터들의 이름과 값을 모두 넣음
		while(parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			Object paramValue = request.getParameter(paramName);
			
			param.put(paramName, paramValue);
		}
		
		return param;
	}

	public static String toJsonStr(Map<String, Object> param) {
		// 맵핑해주는 변수 선언
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(param);
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		return mapper.toString();
	}

	public static String getEncoded(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}
	
	
}
