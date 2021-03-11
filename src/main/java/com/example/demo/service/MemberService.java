package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.GenFile;
import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;
import com.example.demo.util.Util;

@Service
public class MemberService {	
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private GenFileService genFileService;

	public Member getMember(int id) {
		return memberDao.getMember(id);
	}

	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	public ResultData doJoin(Map<String, Object> param) {
		memberDao.doJoin(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		genFileService.changeInputFileRelIds(param, id);
		
		return new ResultData("S-1", "회원 가입이 완료되었습니다.", "id", id);
	}

	public ResultData modifyMember(Map<String, Object> param) {
		memberDao.modifyMember(param);
		
		return new ResultData("S-1", "회원 정보가 수정되었습니다.");
	}

	public boolean isAdmin(Member actor) {
		return actor.getAuthLevel() == 7;
	}

	public Member getMemberByAuthKey(String authKey) {
		return memberDao.getMemberByAuthKey(authKey);
	}

	public List<Member> getForPrintMembers(String searchKeywordType, String searchKeyword, int page,
			int itemsInAPage, Map<String, Object> param) {		
		int limitStart = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		
		param.put("searchKeyword", searchKeyword);
		param.put("searchKeywordType", searchKeywordType);
		param.put("limitStart", limitStart);
		param.put("limitTake", limitTake);
		
		return memberDao.getForPrintMembers(param);
	}

	public static String getAuthLevelName(Member member) {		
		switch( member.getAuthLevel() ) {
		case 7:
			return "관리자";
		case 3:
			return "일반";
		default:
			return "분류되지 않음";
		}
	}

	public static String getAuthLevelNameColor(Member member) {
		switch( member.getAuthLevel() ) {
		case 7:
			return "red";
		case 3:
			return "gray";
		default:
			return "";
		}
	}

	public Member getForPrintMember(Integer id) {
		return memberDao.getForPrintMember(id);
	}

	public Member getForPrintMemberByAuthKey(String authKey) {
		Member member = memberDao.getMemberByAuthKey(authKey);
		
		updateForPrint(member);
		
		return member;
	}

	public Member getForPrintMemberByLoginId(String loginId) {
		Member member = memberDao.getMemberByAuthKey(loginId);
		
		updateForPrint(member);
		
		return member;
	}

	private void updateForPrint(Member member) {
		GenFile genFile = genFileService.getGenFile("member", member.getId(), "common", "attachment", 1);
		
		if(genFile != null) {
			String imgUrl = genFile.getForPrintUrl();
			member.setExtra__thumbImg(imgUrl);
		}
		
	}
	
	

}
