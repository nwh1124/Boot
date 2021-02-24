package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.dto.Member;
import com.example.demo.service.MemberService;

@Component("beforeActionInterceptor") // 컴포넌트 이름 설정
public class BeforeActionInterceptor implements HandlerInterceptor {
	@Autowired
	MemberService memberService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		
		int loginedMemberId = 0;
		Member loginedMember = null;
		
		String authKey = request.getParameter("authKey");
		
		// authKey가 있을 경우 authKey를 이용해 회원 정보를 가져온다
		if(authKey != null && authKey.length() > 0) {
			loginedMember = memberService.getMemberByAuthKey(authKey);
			
			if(loginedMember == null) {
				request.setAttribute("authKeyStatus", "invalid");
			}
			else {
				request.setAttribute("authKeyStatus", "valid");
				loginedMemberId = loginedMember.getId();
			}
		}
		
		// authKey가 없을 경우 세션에서 회원의 정보를 가져온다
		// session에 직접적으로 접근하는 건 줄이는 게 좋음
		else {
			HttpSession session = request.getSession();
			request.setAttribute("authKeyStatus", "none");
			
			if(session.getAttribute("loginedMemberId") != null) {
				loginedMemberId = (int) session.getAttribute("loginedMemberId");
				loginedMember = memberService.getMember(loginedMemberId);
			}
		}
		
		boolean isLogined = false;
		boolean isAdmin = false;
		
		if(loginedMember != null) {
			isLogined = true;
			isAdmin = memberService.isAdmin(loginedMemberId);
		}

		request.setAttribute("loginedMemberId", loginedMemberId);
		request.setAttribute("isLogined", isLogined);
		request.setAttribute("loginedMember", loginedMember);
		request.setAttribute("isAdmin", isAdmin);
		
		return HandlerInterceptor.super.preHandle(request, response, handler) ;
	}

}
