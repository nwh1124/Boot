package com.example.demo.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {

	public void doJoin(Map<String, Object> param);
	public Member getMember(@Param("id") int id);
	public Member getMemberByLoginId(@Param("loginId") String loginId);

}
