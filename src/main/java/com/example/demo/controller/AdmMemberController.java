package com.example.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;
import com.example.demo.service.MemberService;
import com.example.demo.util.Util;

@Controller
public class AdmMemberController {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/adm/member/doJoin")
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param) {
		if(param.get("loginId") == null) {
			return Util.msgAndBack("loginId를 입력해주세요.");
		}
		if(param.get("loginPw") == null) {
			return Util.msgAndBack("비밀번호를 입력해주세요.");
		}
		if(param.get("name") == null) {
			return Util.msgAndBack("이름을 입력해주세요.");
		}
		if(param.get("nickname") == null) {
			return Util.msgAndBack("닉네임을 입력해주세요.");
		}
		if(param.get("email") == null) {
			return Util.msgAndBack("이메일을 입력해주세요.");
		}
		if(param.get("phoneNumber") == null) {
			return Util.msgAndBack("휴대폰 번호를 입력해주세요.");
		}
		
		Member existingMember = memberService.getMemberByLoginId((String)param.get("loginId"));
		
		if(existingMember != null) {
			return Util.msgAndBack(String.format("%s는 이미 존재하는 아이디입니다.", param.get("loginId")));
		}
		
		String msg = String.format("%s님 환영합니다.", existingMember.getNickname());
		
		return Util.msgAndReplace(msg, "../home/main");
	}	
	
	@RequestMapping("/adm/member/login")
	public String login() {
		return "adm/member/login";
	}
	
	@RequestMapping("/adm/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String redirectUrl, HttpServletRequest req) {
		if(loginId == null) {
			return Util.msgAndBack("아이디를 입력해주세요.");
		}
		if(loginPw == null) {
			return Util.msgAndBack("비밀번호를 입력해주세요.");
		}
		
		Member existingMember = memberService.getMemberByLoginId(loginId);
		
		if(existingMember == null) {
			return Util.msgAndBack("존재하지 않는 아이디입니다.");
		}
		
		if(existingMember.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack("비밀번호가 일치하지 않습니다.");
		}
		
		req.setAttribute("loginedMemberId", existingMember.getId());
		
		String msg = String.format("%s님 환영합니다!", existingMember.getNickname()); 
		
		if(redirectUrl == null) {
			return Util.msgAndReplace(msg, "../home/main");
		}
		
		return Util.msgAndReplace(msg, redirectUrl);
	}
	
	@RequestMapping("/adm/member/doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {
		session.removeAttribute("loginedMemberId");
		
		return Util.msgAndReplace("로그아웃 되었습니다.", "../member/login");
	}
	
	@RequestMapping("/adm/member/doModify")
	@ResponseBody
	public String doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		if(param.isEmpty()) {
			return Util.msgAndBack("수정할 정보를 입력해주세요.");
		}
		
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		ResultData rdMsg = memberService.modifyMember(param);
		return Util.msgAndBack(rdMsg.getMsg());
	}

}
