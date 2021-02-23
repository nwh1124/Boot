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
	@Autowired
	private MemberService memberService;
	
	public Article getArticle(Map<String, Object> param) {		
		return articleDao.getArticle(param);
	}

	public List<Article> getForPrintArticles(Map<String, Object> param) {
		int page = (int)param.get("page");
		int itemsInAPage = (int)param.get("itemsInAPage");
		
		int limitStart = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		
		param.put("limitStart", limitStart);
		param.put("limitTake", limitTake);
		
		return articleDao.getForPrintArticles(param);
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

	public ResultData getActorCanDeleteRd(Article article, int actorId) {
		if(article.getMemberId() == actorId) {
			return new ResultData("S-1", "가능합니다.");
		}
		
		if(memberService.isAdmin(actorId)) {
			return new ResultData("S-2", "가능합니다.");
		}
		
		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData getActorCanModifyRd(Article article, int actorId) {
		return getActorCanDeleteRd(article, actorId);
	}

	public Article getForPrintArticle(Integer id) {		
		return articleDao.getForPrintArticle(id);
	}

}
