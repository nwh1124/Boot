package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Article;
import com.example.demo.dto.Board;
import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;
import com.example.demo.service.ArticleService;

@Controller
public class UsrArticleController {
	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/usr/article/list")
	@ResponseBody
	public ResultData showList(@RequestParam(defaultValue = "1") int boardId, String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		
		Board board = articleService.getBoard(boardId);
		
		if(board == null) {
			return new ResultData("F-1", "존재하지 않는 게시판입니다.");
		}
		
		if(searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}		
		
		if(searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}
		
		if(searchKeyword == null) {
			searchKeyword = "";
		}
		
		if(searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = "";
		}
		
		if(searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}
		
		int itemsInAPage = 20;
		
		Map<String, Object> param = new HashMap<>();
		param.put("searchKeywordType", searchKeywordType);
		param.put("searchKeyword", searchKeyword);
		param.put("page", page);
		param.put("itemsInAPage", itemsInAPage);
		param.put("boardId", boardId);
		
		List<Article> articles = articleService.getForPrintArticles(param);
		return new ResultData("S-1", "성공", "articles", articles);
	}
	
	@GetMapping("/usr/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
		
		if(id == null) {
			return new ResultData("F-1", "게시물 번호를 입력해주세요.");
		}
		
		Article article = articleService.getForPrintArticle(id);
		
		if(article == null) {
			return new ResultData("F-2", String.format("%d번 게시물은 존재하지 않습니다.", id));
		}
		
		return new ResultData("S-1", "성공", "article", article);
	}
	
	@PostMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		
		if(param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if(param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		param.put("memberId", loginedMemberId);
		
		return articleService.addArticle(param);
	}
	
	@PostMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		Member loginedMember = (Member)req.getAttribute("loginedMember");
		
		if(param.get("id") == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		
		Article article = articleService.getArticle(param);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", param.get("id"));
		}
		
		ResultData actorCanDeleteRd = articleService.getActorCanDeleteRd(article, loginedMember);
		
		if(actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}
		
		return articleService.deleteArticle(param);
	}
	
	@PostMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		Member loginedMember = (Member)req.getAttribute("loginedMember");
		
		if(param.get("id") == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		if(param.get("title") == null && param.get("body") == null) {
			return new ResultData("F-1", "수정할 제목 또는 내용을 입력해주세요.");
		}		
		
		Article article = articleService.getArticle(param);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", param.get("id"));
		}
		
		ResultData actorCanModifyRd = articleService.getActorCanModifyRd(article, loginedMember);
		
		if(actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		
		return articleService.modifyArticle(param);
	}

}
