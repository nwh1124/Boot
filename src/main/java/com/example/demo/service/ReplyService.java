package com.example.demo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ReplyDao;
import com.example.demo.dto.Reply;
import com.example.demo.dto.ResultData;
import com.example.demo.util.Util;

@Service
public class ReplyService {
	@Autowired
	ReplyDao replyDao;
	@Autowired
	MemberService memberService;

	public ResultData addReply(Map<String, Object> param, HttpServletRequest req) {
		replyDao.addReply(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		return new ResultData("S-1", "댓글이 작성되었습니다.", "id", id);
	}

	public List<Reply> getForPrintReplies(String relTypeCode, Integer relId) {
		return replyDao.getForPrintReplies(relTypeCode, relId);
	}

	public Reply getForPrintReply(Integer id) {
		return replyDao.getForPrintReply(id);
	}

	public ResultData getActorCanDeleteRd(Reply reply, int actorId) {
		if(reply.getMemberId() == actorId) {
			return new ResultData("S-1", "가능합니다.");
		}
		
		if(memberService.isAdmin(actorId)) {
			return new ResultData("S-1", "가능합니다.");
		}
		
		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData deleteReply(Integer id) {
		replyDao.deleteReply(id);
		
		return new ResultData("S-1", "삭제되었습니다.", "id", id);
	}

}
