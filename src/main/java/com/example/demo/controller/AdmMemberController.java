package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;
import com.example.demo.service.MemberService;
import com.example.demo.util.Util;

@Controller
public class AdmMemberController extends BaseController{
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/adm/member/getLoginIdDup")
	@ResponseBody
	public ResultData getLoginIdDup(String loginId) {
		if(loginId == null) {
			return new ResultData("F-5", "loginId를 입력해주세요.");
		}
		
		if (Util.allNumberString(loginId)) {
			return new ResultData("F-3", "로그인아이디는 숫자만으로 구성될 수 없습니다.");
		}

		if (Util.startsWithNumberString(loginId)) {
			return new ResultData("F-4", "로그인아이디는 숫자로 시작할 수 없습니다.");
		}

		if (loginId.length() < 5) {
			return new ResultData("F-5", "로그인아이디는 5자 이상으로 입력해주세요.");
		}

		if (loginId.length() > 20) {
			return new ResultData("F-6", "로그인아이디는 20자 이하로 입력해주세요.");
		}

		if (Util.isStandardLoginIdString(loginId) == false) {
			return new ResultData("F-1", "로그인아이디는 영문소문자와 숫자의 조합으로 구성되어야 합니다.");
		}
		
		Member existingMember = memberService.getMemberByLoginId(loginId);
		
		if(existingMember == null) {
			return new ResultData("F-2", String.format("%s(은)는 이미 사용 중인 로그인 아이디입니다.", loginId));
		}
		
		return new ResultData("S-1", String.format("%s(은)는 사용 가능한 로그인 아이디입니다.", loginId));		
	}
	
	@RequestMapping("/adm/member/join")
	public String showJoin() {
		return "adm/member/join";
	}
	
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
		
		memberService.doJoin(param);
		
		existingMember = memberService.getMemberByLoginId((String)param.get("loginId"));
		
		String msg = String.format("%s님 환영합니다.", existingMember.getNickname());
		
		return Util.msgAndReplace(msg, "../home/main");
	}	
	
	@RequestMapping("/adm/member/login")
	public String showlogin() {
		return "adm/member/login";
	}
	
	@RequestMapping("/adm/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String redirectUrl, HttpSession session) {
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

		session.setAttribute("loginedMemberId", existingMember.getId());
		
		String msg = String.format("%s님 환영합니다!", existingMember.getNickname()); 
		
		redirectUrl = Util.ifEmpty(redirectUrl, "../home/main");

		return Util.msgAndReplace(msg, redirectUrl);
	}
	
	@RequestMapping("/adm/member/doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {
		session.removeAttribute("loginedMemberId");
		
		return Util.msgAndReplace("로그아웃 되었습니다.", "../member/login");
	}
	
	@RequestMapping("/adm/member/modify")
	public String showModify(Integer id, HttpServletRequest req) {		
		if(id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}
		
		Member member = memberService.getForPrintMember(id);
		
		if(member == null) {
			return msgAndBack(req, "존재하지 않는 회원번호입니다.");
		}
		
		req.setAttribute("member", member);
		
		return "adm/member/modify";
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
	
	@RequestMapping("/adm/member/list")
	public String showList(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId,
				String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page,
				@RequestParam Map<String, Object> param) {
		if (searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}

		if (searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "name";
		}

		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		if (searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}

		if (searchKeyword == null) {
			searchKeywordType = null;
		}
		
		int itemsInAPage = 20;
		
		List<Member> members = memberService.getForPrintMembers(searchKeywordType, searchKeyword, page, itemsInAPage, param);
		
		req.setAttribute("members", members);
	
		return "adm/member/list";
	}


}
