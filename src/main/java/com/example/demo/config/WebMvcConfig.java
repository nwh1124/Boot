package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
	// beforeActionInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("beforeActionInterceptor")
	HandlerInterceptor beforeActionInterceptor;
	
	// needAdminInterceptor 관리자 권한 인터셉터 불러오기
	@Autowired
	@Qualifier("needAdminInterceptor")
	HandlerInterceptor needAdminInterceptor;

	// needToLoginInterceptor 로그인 필요 인터셉터 불러오기
	@Autowired
	@Qualifier("needLoginInterceptor")
	HandlerInterceptor needLoginInterceptor;

	// needToLogoutInterceptor 로그인 필요 인터셉터 불러오기
	@Autowired
	@Qualifier("needLogoutInterceptor")
	HandlerInterceptor needLogoutInterceptor;
	

	// 이 함수는 인터셉터를 적용하는 역할을 합니다.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// beforeActionInterceptor 인터셉터가 모든 액션 실행전에 실행되도록 처리
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**");

		// 어드민 필요
		registry.addInterceptor(needAdminInterceptor)
			.addPathPatterns("/adm/**")
			.excludePathPatterns("/adm/member/login")
			.excludePathPatterns("/adm/member/doLogin");
		
		// 로그인 필요 인터셉터
		registry.addInterceptor(needLoginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/adm/**")
				.excludePathPatterns("/resource/**")
				.excludePathPatterns("/usr/home/**")
				.excludePathPatterns("/usr/member/authKey")
				.excludePathPatterns("/usr/member/login")
				.excludePathPatterns("/usr/member/doLogin")
				.excludePathPatterns("/usr/member/join")
				.excludePathPatterns("/usr/member/doJoin")
				.excludePathPatterns("/usr/article/list")
				.excludePathPatterns("/usr/article/detail")
				.excludePathPatterns("/usr/reply/list")
				.excludePathPatterns("/usr/member/findLoginId")
				.excludePathPatterns("/usr/member/doFindLoginId")
				.excludePathPatterns("/usr/member/findLoginPw")
				.excludePathPatterns("/usr/member/doFindLoginPw")
				.excludePathPatterns("/usr/file/test*")
				.excludePathPatterns("/usr/file/doTest*")
				.excludePathPatterns("/test/**")
				.excludePathPatterns("/error");
		
		registry.addInterceptor(needLogoutInterceptor)
			.addPathPatterns("/usr/adm/login")
			.addPathPatterns("/usr/adm/doLogin")
				.addPathPatterns("/usr/member/login")
				.addPathPatterns("/usr/member/doLogin")
				.addPathPatterns("/usr/member/join")
				.addPathPatterns("/usr/member/doJoin");
		
	}	
	
}
