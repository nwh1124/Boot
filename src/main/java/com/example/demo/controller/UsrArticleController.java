package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Article;

@Controller
public class UsrArticleController {
	
	private List<Article> articles = new ArrayList<>();
	
	public UsrArticleController() {
		articles.add(new Article(1, "2021-02-16 21:21:21", "제목1", "내용1"));
		articles.add(new Article(2, "2021-02-16 21:21:21", "제목2", "내용2"));
		articles.add(new Article(3, "2021-02-16 21:21:21", "제목3", "내용3"));
		articles.add(new Article(4, "2021-02-16 21:21:21", "제목4", "내용4"));
	}
	
	@RequestMapping("/usr/article/list")
	@ResponseBody
	public List<Article> showList() {
		return articles;
	}
	
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public Article showDetail(int id) {
			return articles.get(id - 1);
	}
}
