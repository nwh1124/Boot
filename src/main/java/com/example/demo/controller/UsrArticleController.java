package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Article;
import com.example.demo.dto.ResultData;
import com.example.demo.service.ArticleService;

@Controller
public class UsrArticleController {
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/article/list")
	@ResponseBody
	public List<Article> showList(String searchKeywordType, String searchKeyword) {
		
		if(searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}		
		
		if(searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}
		
		if(searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}
		
		if(searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}
		
		Map<String, Object> param = new HashMap<>();
		param.put("searchKeywordType", searchKeywordType);
		param.put("searchKeyword", searchKeyword);
		
		System.out.println(param);
		
		return articleService.getArticles(param);
	}
	
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public Article showDetail(@RequestParam Map<String, Object> param) {
		Article article = articleService.getArticle(param);
		return article;
	}
	
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param) {
		if(param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if(param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		return articleService.addArticle(param);
	}
	
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(@RequestParam Map<String, Object> param) {
		if(param.get("id") == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		
		Article article = articleService.getArticle(param);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", param.get("id"));
		}
		
		return articleService.deleteArticle(param);
	}
	
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {
		if(param.get("id") == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		if(param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if(param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		Article article = articleService.getArticle(param);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", param.get("id"));
		}
		
		return articleService.modifyArticle(param);
	}

}
