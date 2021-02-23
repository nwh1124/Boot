package com.example.demo.controller;

import java.util.Map;

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
public class UsrMemberController {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		if(param.get("loginId") == null) {
			return new ResultData("F-1", "아이디를 입력해주세요.");
		}
		if(param.get("loginPw") == null) {
			return new ResultData("F-1", "비밀번호를 입력해주세요.");
		}
		if(param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해주세요.");
		}
		if(param.get("nickname") == null) {
			return new ResultData("F-1", "닉네임을 입력해주세요.");
		}
		if(param.get("email") == null) {
			return new ResultData("F-1", "이메일을 입력해주세요.");
		}
		if(param.get("phoneNumber") == null) {
			return new ResultData("F-1", "휴대폰 번호를 입력해주세요.");
		}
		
		Member existingMember = memberService.getMemberByLoginId((String)param.get("loginId"));
		
		if(existingMember != null) {
			return new ResultData("F-2", String.format("%s는 이미 존재하는 아이디입니다.", param.get("loginId")));
		}
		
		return memberService.doJoin(param);
	}	
	
	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public ResultData doLogin(String loginId, String loginPw, HttpSession session) {
		if(loginId == null) {
			return new ResultData("F-1", "아이디를 입력해주세요.");
		}
		if(loginPw == null) {
			return new ResultData("F-1", "비밀번호를 입력해주세요.");
		}
		
		Member existingMember = memberService.getMemberByLoginId(loginId);
		
		if(existingMember == null) {
			return new ResultData("F-2", "존재하지 않는 아이디입니다.");
		}
		
		if(existingMember.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}
		
		session.setAttribute("loginedMemberId", existingMember.getId());
		
		return new ResultData("S-1", String.format("%s님 환영합니다!", existingMember.getNickname()));
	}
	
	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public ResultData doLogout(HttpSession session) {		
		if(session.getAttribute("loginedMemberId") == null) {
			return new ResultData("F-1", "로그인 중이 아닙니다.");
		}
		
		session.removeAttribute("loginedMemberId");
		
		return new ResultData("S-1", "로그아웃 되었습니다.");
	}
	
	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpSession session) {
		if(session.getAttribute("loginedMemberId") == null) {
			return new ResultData("F-1", "로그인 후 이용해주세요.");
		}
		
		if(param.isEmpty()) {
			return new ResultData("F-2", "수정할 정보를 입력해주세요.");
		}
		
		int loginedMemberId = (int)session.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		return memberService.modifyMember(param);
	}

}
