package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;

@Mapper
public interface MemberDao {

	public void doJoin(Map<String, Object> param);
	public Member getMember(@Param("id") int id);
	public Member getMemberByLoginId(@Param("loginId") String loginId);
	public void modifyMember(Map<String, Object> param);
	public Member getMemberByAuthKey(@Param("authKey") String authKey);
	public List<Member> getForPrintMembers(@Param("searchKeywordType") String searchKeywordType, @Param("searchKeyword") String searchKeyword,
			@Param("limitStart") int limitStart, @Param("limitTake") int limitTake);

}
