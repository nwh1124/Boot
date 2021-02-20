package com.example.demo.dto;

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
		private String loginPw;
		private String name;
		private String nickname;
		private String email;
		private String phoneNumber;	

}
