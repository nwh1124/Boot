package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

// 생성자 자동 생성
@NoArgsConstructor
@AllArgsConstructor

public class Member {
		
		private int id;
		private String regDate;
		private String updateDate;
		private String loginId;
		@JsonIgnore
		private String loginPw;
		@JsonIgnore
		private String authKey;
		private String name;
		private String nickname;
		private String email;
		private String phoneNumber;	

}
