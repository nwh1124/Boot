package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.Article;
import com.example.demo.dto.Board;

@Mapper
public interface ArticleDao {	
	public Article getArticle(Map<String, Object> param);
	public void addArticle(Map<String, Object> param);
	public void deleteArticle(Map<String, Object> param);
	public void modifyArticle(Map<String, Object> param);
	public List<Article> getForPrintArticles(Map<String, Object> param);
	public Article getForPrintArticle(@Param("id") Integer id);
	public Board getBoard(@Param("id") int id);
}
