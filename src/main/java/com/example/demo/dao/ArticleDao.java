package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Article;

@Mapper
public interface ArticleDao {	
	public Article getArticle(Map<String, Object> param);
	public void addArticle(Map<String, Object> param);
	public void deleteArticle(Map<String, Object> param);
	public void modifyArticle(Map<String, Object> param);
	public List<Article> getArticles(Map<String, Object> param);
	public Article getForPrintArticle(Integer id);
}
