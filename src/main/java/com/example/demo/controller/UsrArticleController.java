package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
		if( searchKeywordType != null ) {
			searchKeywordType = searchKeywordType.trim();
		}
		
		if( searchKeywordType == null || searchKeywordType.length() == 0 ) {
			searchKeywordType = "titleAndBody";
		}
		
		if( searchKeyword != null && searchKeyword.length() == 0 ) {
			searchKeyword = null;
		}
		
		if( searchKeyword != null ) {
			searchKeyword = searchKeyword.trim();
		}
		
		return articleService.getArticles(searchKeywordType, searchKeyword);
	}
	
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public Article showDetail(Integer id) {
		Article article = articleService.getArticleById(id);
		return article;
	}
	
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(String title, String body) {
		if(title == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if(body == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		return articleService.add(title, body);
	}
	
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id) {
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		
		Article article = articleService.getArticleById(id);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}
		
		return articleService.deleteArticle(id);
	}
	
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body) {
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		if(title == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if(body == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		Article article = articleService.getArticleById(id);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}
		
		return articleService.modifyArticle(id, title, body);
	}

}
