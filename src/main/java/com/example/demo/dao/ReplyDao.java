package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.Reply;

@Mapper
public interface ReplyDao {

	public void addReply(Map<String, Object> param);
	public List<Reply> getForPrintReplies(@Param("relTypeCode") String relTypeCode, @Param("relId") Integer relId);
	public Reply getForPrintReply(@Param("id") Integer id);
	public void deleteReply(Integer id);
	public void modifyReply(@Param("id") int id, @Param("body") String body); 

}
