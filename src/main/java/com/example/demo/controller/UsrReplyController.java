package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Article;
import com.example.demo.dto.Reply;
import com.example.demo.dto.ResultData;
import com.example.demo.service.ArticleService;
import com.example.demo.service.ReplyService;
import com.example.demo.util.Util;

@Controller
public class UsrReplyController {
	@Autowired
	private ReplyService replyService;
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/reply/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpSession session) {
		if(param.get("relTypeCode") == null) {
			return new ResultData("F-1", "관련 타입 코드를 입력해주세요.");
		}
		if(param.get("body") == null) {
			return new ResultData("F-1", "댓글 내용을 입력해주세요.");
		}
		
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
		param.put("memberId", loginedMemberId);
		
		return replyService.addReply(param, session);
		
	}
	
	@RequestMapping("/usr/reply/list")
	@ResponseBody
	public ResultData showList(String relTypeCode, Integer relId) {
		
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

}
