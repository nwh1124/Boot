package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ArticleDao;
import com.example.demo.dto.Article;
import com.example.demo.dto.Board;
import com.example.demo.dto.GenFile;
import com.example.demo.dto.Member;
import com.example.demo.dto.ResultData;
import com.example.demo.util.Util;

@Service
public class ArticleService {
	
	@Autowired
	private GenFileService genFileService;
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
		
		List<Article> articles = articleDao.getForPrintArticles(param);
		List<Integer> articleIds = articles.stream().map(article -> article.getId()).collect(Collectors.toList());
		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo("article", articleIds, "common", "attachment");
		
		for(Article article : articles) {
			Map<String, GenFile> mapByFileNo = filesMap.get(article.getId());
			
			if(mapByFileNo != null) {
				article.getExtraNotNull().put("file__common__attachment", mapByFileNo);
			}
		}
		
		return articles;
	}

	public ResultData addArticle(Map<String, Object> param) {		
		articleDao.addArticle(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		genFileService.changeInputFileRelIds(param, id);
		
		return new ResultData("S-1", "게시물이 등록되었습니다.", "id", id);
	}

	public ResultData deleteArticle(Map<String, Object> param) {		
		articleDao.deleteArticle(param);
		
		genFileService.deleteGenFiles("article", Integer.parseInt((String)param.get("id")));
		
		return new ResultData("S-1", "게시물이 삭제되었습니다.", "id", param.get("id"));
	}

	public ResultData modifyArticle(Map<String, Object> param) {
		articleDao.modifyArticle(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		genFileService.changeInputFileRelIds(param, id);
		
		return new ResultData("S-1", "게시물이 수정되었습니다.", "id", param.get("id"));
	}

	public ResultData getActorCanDeleteRd(Article article, Member actor) {
		if(article.getMemberId() == actor.getId()) {
			return new ResultData("S-1", "가능합니다.");
		}
		
		if(memberService.isAdmin(actor)) {
			return new ResultData("S-2", "가능합니다.");
		}
		
		return new ResultData("F-1", "권한이 없습니다.");
	}

	public ResultData getActorCanModifyRd(Article article, Member actor) {
		return getActorCanDeleteRd(article, actor);
	}

	public Article getForPrintArticle(Integer id) {		
		return articleDao.getForPrintArticle(id);
	}

	public Board getBoard(int id) {		
		return articleDao.getBoard(id);
	}

}
