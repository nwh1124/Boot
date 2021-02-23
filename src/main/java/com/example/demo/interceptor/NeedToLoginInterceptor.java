package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component("needToLoginInterceptor") // 컴포넌트 이름 설정
public class NeedToLoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// beforeActionInterceptor에서 선언한 정보를 모두 쓸 수 있음
		
		boolean isLogined = (boolean) request.getAttribute("isLogined");
		boolean isAjax = true;
		
		if(isLogined == false) {
			if(isAjax == false) {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().append("<script>");
				response.getWriter().append("alert('로그인 후 이용해주세요.')");
				response.getWriter().append("location.replace('/usr/member/login?redirectUri=" 
						+ request.getAttribute("encodedAfterLoginUri") + "');");
				response.getWriter().append("</script>");
				
				// 리턴 false;를 이후에 실행될 인터셉터와 액션이 실행되지 않음
				// 잘 이해가 안 가니 좀 더 해보자
			}
			else {
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().append("{\"resultCode\":\"F-A\",\"msg\":\"로그인 후 이용해주세요.\"");
			}
			
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
}
