package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.example.demo.dto.Article;
import com.example.demo.dto.Board;
import com.example.demo.dto.GenFile;
import com.example.demo.dto.ResultData;
import com.example.demo.service.ArticleService;
import com.example.demo.service.GenFileService;
import com.example.demo.util.Util;

@Controller
public class AdmArticleController extends BaseController{
	@Autowired
	private ArticleService articleService;
	@Autowired
	private GenFileService genFileService;
	
	@RequestMapping("/adm/article/list")
	public String showList(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId, 
			String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		
		Board board = articleService.getBoard(boardId);
		
		req.setAttribute("board", board);
		
		if(board == null) {
			return msgAndBack(req, "존재하지 않는 게시판입니다.");
		}
		
		if(searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}		
		
		if(searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}
		
		if(searchKeyword == null) {
			searchKeyword = "";
		}
		
		if(searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = "";
		}
		
		if(searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}
		
		int itemsInAPage = 20;
		
		Map<String, Object> param = new HashMap<>();
		param.put("searchKeywordType", searchKeywordType);
		param.put("searchKeyword", searchKeyword);
		param.put("page", page);
		param.put("itemsInAPage", itemsInAPage);
		param.put("boardId", boardId);
		
		List<Article> articles = articleService.getForPrintArticles(param);
		
		for(Article article : articles) {
			GenFile genFile = genFileService.getGenFile("article", article.getId(), "common", "attachment", 1);
			
			if(genFile != null) {
				article.setExtra__thumbImg(genFile.getForPrintUrl());				
			}
		}
		
		req.setAttribute("articles", articles);
		
		return "adm/article/list";
	}
	
	@RequestMapping("/adm/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
		
		if(id == null) {
			return new ResultData("F-1", "게시물 번호를 입력해주세요.");
		}
		
		Article article = articleService.getForPrintArticle(id);
		
		if(article == null) {
			return new ResultData("F-2", String.format("%d번 게시물은 존재하지 않습니다.", id));
		}
		
		return new ResultData("S-1", "성공", "article", article);
	}
	
	@RequestMapping("/adm/article/doAdd")
	public String doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req,
			MultipartRequest multipartRequest) {
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		
		if(param.get("title") == null) {
			return msgAndBack(req, "title을 입력해주세요.");
		}
		if(param.get("body") == null) {
			return msgAndBack(req, "body를 입력해주세요.");
		}
		
		param.put("memberId", loginedMemberId);
		
		ResultData addArticleRd = articleService.addArticle(param);

		int newArticleId = (int) addArticleRd.getBody().get("id");

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);
			if(multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile);
			}
		}

		return msgAndReplace(req, String.format("%d번 게시물이 작성되었습니다.", newArticleId), "../article/detail?id=" + newArticleId);
	}

	@RequestMapping("/adm/article/add")
	public String add(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "adm/article/add";
	}
	
	@RequestMapping("/adm/article/doDelete")
	@ResponseBody
	public ResultData doDelete(@RequestParam Map<String, Object> param, HttpServletRequest req) {

		if(param.get("id") == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		int id = Integer.parseInt((String)param.get("id"));		
		
		Article article = articleService.getArticle(param);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", id);
		}
		
		ResultData actorCanDeleteRd = articleService.getActorCanDeleteRd(article, loginedMemberId);
		
		if(actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}
		
		return articleService.deleteArticle(param);
	}
	
	@RequestMapping("/adm/article/modify")
	public String showModify(Integer id, HttpServletRequest req) {
		if(id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}
		
		Article article = articleService.getForPrintArticle(id);
		
		List<GenFile> files = genFileService.getGenFiles("article", article.getId(), "common", "attachment");
		
		Map<String, GenFile> filesMap = new HashMap<>();
		
		for(GenFile file : files) {
			filesMap.put(file.getFileNo() + "", file);
		}
		
		article.getExtraNotNull().put("file__common__attachment", filesMap);
		req.setAttribute("article", article);
		
		if(article == null) {
			return msgAndBack(req, "존재하지 않는 게시물 번호입니다.");
		}
		
		return "adm/article/modify";
	}
	
	@RequestMapping("/adm/article/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		if(id == 0) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		if(Util.isEmpty(param.get("title")) && Util.isEmpty(param.get("body"))) {
			return new ResultData("F-1", "수정할 제목 또는 내용을 입력해주세요.");
		}		
		
		Article article = articleService.getArticle(param);
		
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", param.get("id"));
		}
		
		ResultData actorCanModifyRd = articleService.getActorCanModifyRd(article, loginedMemberId);
		
		if(actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		
		return articleService.modifyArticle(param);
	}	

}
