package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Article;
import com.example.demo.dto.Member;
import com.example.demo.dto.Reply;
import com.example.demo.dto.ResultData;
import com.example.demo.service.ArticleService;
import com.example.demo.service.ReplyService;

@Controller
public class UsrReplyController {
	@Autowired
	private ReplyService replyService;
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/reply/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		if(param.get("relTypeCode") == null) {
			return new ResultData("F-1", "관련 타입 코드를 입력해주세요.");
		}
		if(param.get("body") == null) {
			return new ResultData("F-1", "댓글 내용을 입력해주세요.");
		}
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		
		param.put("memberId", loginedMemberId);
		
		return replyService.addReply(param, req);
		
	}
	
	@RequestMapping("/usr/reply/list")
	@ResponseBody
	public ResultData showList(String relTypeCode, Integer relId) {
		
		if(relTypeCode == null) {
			return new ResultData("F-1", "관련 타입 코드를 입력해주세요.");
		}
		
		if(relId == null) {
			return new ResultData("F-1", "관련 타입 번호를 입력해주세요.");
		}
		
		if(relTypeCode.equals("article")) {
			Article article = articleService.getForPrintArticle(relId);
			
			if(article == null) {
				return new ResultData("F-1", "존재하지 않는 게시물입니다.");
			}
			
		}
		
		List<Reply> replies = replyService.getForPrintReplies(relTypeCode, relId);
		
		return new ResultData("S-1", "성공", "replies", replies);
	}
	
	@RequestMapping("/usr/reply/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpServletRequest req) {
		Member loginedMember = (Member) req.getAttribute("loginedMember");
		
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		
		Reply reply = replyService.getForPrintReply(id);
		
		if(reply == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}
		
		ResultData actorCanDeleteRd = replyService.getActorCanDeleteRd(reply, loginedMember);
		
		if(actorCanDeleteRd.isFail()){
			return actorCanDeleteRd;
		}
		
		return replyService.deleteReply(id);
	}

}
