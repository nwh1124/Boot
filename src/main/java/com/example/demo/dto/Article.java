package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

// 생성자 자동 생성
@NoArgsConstructor
@AllArgsConstructor

public class Article {
		
		private int id;
		private String regDate;
		private String updateDate;
		private int boardId;
		private int memberId;
		private String title;
		private String body;
		String extra__writer;

}
