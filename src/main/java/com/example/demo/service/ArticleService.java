package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.Article;
import com.example.demo.dto.ResultData;
import com.example.demo.util.Util;

@Service
public class ArticleService {
	
	private int articlesLastId;
	private List<Article> articles;
	
	public ArticleService() {
		articlesLastId = 0;
		articles = new ArrayList<>();
		
		articles.add(new Article(++articlesLastId, "2021-02-16 21:21:21", "2021-02-16 21:21:21", "제목1", "내용1"));
		articles.add(new Article(++articlesLastId, "2021-02-16 21:21:21", "2021-02-16 21:21:21", "제목2", "내용2"));
	}

	public Article getArticleById(int id) {		
		for(Article article : articles) {
			if(article.getId() == id) {
				return article;
			}
		}
		
		return null;
	}

	public List<Article> getArticlesForPrint() {
		return articles;
	}

	public ResultData add(String title, String body) {
		String regDate = Util.getNowDateStr();
		String updateDate = regDate;
		
		articles.add(new Article(++articlesLastId, regDate, updateDate, title, body));
		
		return new ResultData("S-1", "게시물이 등록되었습니다.", "id", articlesLastId);
	}

	public ResultData deleteArticle(int id) {		
		for(Article article : articles) {
			if(article.getId() == id) {
				articles.remove(article);
				return new ResultData("S-1", "게시물이 삭제되었습니다.", "id", id);
			}
		}
		
		return new ResultData("F-1", "게시물이 존재하지 않습니다.", "id", id);
	}

	public ResultData modifyArticle(int id, String title, String body) {		
		Article modArticle = getArticleById(id);
		String updateDate = Util.getNowDateStr();
		
		modArticle.setUpdateDate(updateDate);
		modArticle.setTitle(title);
		modArticle.setBody(body);
		
		return new ResultData("S-1", String.format("%d번 게시물이 수정되었습니다.", id));
	}

}
