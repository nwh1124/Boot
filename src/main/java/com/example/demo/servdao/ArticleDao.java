package com.example.demo.servdao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.Article;
import com.example.demo.util.Util;

@Component
public class ArticleDao {
	
	private int articlesLastId;
	private List<Article> articles;
	
	public ArticleDao() {
		articlesLastId = 0;
		articles = new ArrayList<>();
		
		for(int i = 1; i <= 10; i ++) {
			articles.add(new Article(++articlesLastId, Util.getNowDateStr(), Util.getNowDateStr(), "제목 " + i, "내용 " + i));
		}
	}

	public Article getArticleById(int id) {		
		for(Article article : articles) {
			if(article.getId() == id) {
				return article;
			}
		}
		
		return null;
	}

	public List<Article> getArticle(String searchKeywordType, String searchKeyword) {
		if(searchKeyword == null) {
			return articles;
		}
		
		List<Article> filtered = new ArrayList<>();
		
		for(Article article : articles) {
			boolean contains = false;

			if( searchKeywordType.equals("title") ) {
				contains = article.getTitle().contains(searchKeyword);
			}
			else if( searchKeywordType.equals("body") ) {
				contains = article.getBody().contains(searchKeyword);
			}
			else {
				contains = article.getTitle().contains(searchKeyword);
				
				if( contains == false ) {
					contains = article.getBody().contains(searchKeyword);
				}
			}
			
			if( contains ) {
				filtered.add(article);
			}			
		}
		
		return filtered;
	}

	public int addArticle(String title, String body) {
		String regDate = Util.getNowDateStr();
		String updateDate = regDate;
		
		articles.add(new Article(++articlesLastId, regDate, updateDate, title, body));
				
		return articlesLastId;
	}

	public boolean deleteArticle(int id) {
		for(Article article : articles) {
			if(article.getId() == id) {
				articles.remove(article);
				return true;
			}
		}
		
		return false;
	}

	public boolean modifyArticle(int id, String title, String body) {
		Article modArticle = getArticleById(id);
		String updateDate = Util.getNowDateStr();
		
		if(modArticle == null) {
			return false;
		}
		
		modArticle.setUpdateDate(updateDate);
		modArticle.setTitle(title);
		modArticle.setBody(body);
		
		return true;
	}

}
