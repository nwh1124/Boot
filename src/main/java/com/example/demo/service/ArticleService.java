package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Article;
import com.example.demo.dto.ResultData;
import com.example.demo.servdao.ArticleDao;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleDao articleDao;
	
	public Article getArticleById(int id) {		
		return articleDao.getArticleById(id);
	}

	public List<Article> getArticles(String searchKeywordType, String searchKeyword) {
		return articleDao.getArticle(searchKeywordType, searchKeyword);
	}

	public ResultData add(String title, String body) {
		int id = articleDao.addArticle(title, body);		
		
		return new ResultData("S-1", "게시물이 등록되었습니다.", "id", id);
	}

	public ResultData deleteArticle(int id) {		
		boolean rs = articleDao.deleteArticle(id);
		
		if(rs == false) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", id);
		}
		
		return new ResultData("S-1", "게시물이 삭제되었습니다.", "id", id);
	}

	public ResultData modifyArticle(int id, String title, String body) {
		boolean rs = articleDao.modifyArticle(id, title, body);
		
		if(rs == false) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", id);
		}
		
		return new ResultData("S-1", "%게시물이 수정되었습니다.", "id", id);
	}

}
