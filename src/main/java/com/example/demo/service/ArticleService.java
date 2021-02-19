package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ArticleDao;
import com.example.demo.dto.Article;
import com.example.demo.dto.ResultData;
import com.example.demo.util.Util;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleDao articleDao;
	
	public Article getArticle(Map<String, Object> param) {		
		return articleDao.getArticle(param);
	}

	public List<Article> getArticles(Map<String, Object> param) {
		return articleDao.getArticles(param);
	}

	public ResultData addArticle(Map<String, Object> param) {		
		articleDao.addArticle(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		return new ResultData("S-1", "게시물이 등록되었습니다.", "id", id);
	}

	public ResultData deleteArticle(Map<String, Object> param) {		
		articleDao.deleteArticle(param);
		
		return new ResultData("S-1", "게시물이 삭제되었습니다.", "id", param.get("id"));
	}

	public ResultData modifyArticle(Map<String, Object> param) {
		articleDao.modifyArticle(param);
		
		return new ResultData("S-1", "게시물이 수정되었습니다.", "id", param.get("id"));
	}

}
