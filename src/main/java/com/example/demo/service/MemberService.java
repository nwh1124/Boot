package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;
import com.example.demo.util.Util;

@Service
public class MemberService {
	
	@Autowired
	private MemberDao memberDao;

	public Member getMember(int id) {
		return memberDao.getMember(id);
	}

	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	public ResultData doJoin(Map<String, Object> param) {
		memberDao.doJoin(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		return new ResultData("S-1", "회원 가입이 완료되었습니다.", "id", id);
	}

	public ResultData modifyMember(Map<String, Object> param) {
		memberDao.modifyMember(param);
		
		return new ResultData("S-1", "회원 정보가 수정되었습니다.");
	}

	public boolean isAdmin(int actorId) {
		return actorId == 1;
	}

	public boolean isAdmin(Member actor) {
		return isAdmin(actor.getId());
	}

}
