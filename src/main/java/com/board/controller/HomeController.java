package com.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.board.user.domain.UserVo;
import com.board.user.mapper.UserMapper;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserMapper userMapper;
	
	// http://localhost:9090
	@RequestMapping("/")
	public  String   home() {
		return "home";
	}
	
	//  /loginForm
	@RequestMapping("/loginForm")
	public ModelAndView loginForm() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("users/login");
		return mv;
		
	}
	
	// /login (userid=sky, passwd=1234)
	@RequestMapping("/login")
	// public ModelAndView login( @Param String userid, @Param String passwd, HttpServletRequest request ) {
	public ModelAndView login( HttpServletRequest request ) {
				
		// a tag 로 오든, form tag 로 오든 request 로 받을 수 있음
		String userid = request.getParameter("userid");
		String passwd = request.getParameter("passwd");
		
		// DB 에서 로그인 정보 가져올 것 (db 조작 = mapper 가 함)
		// userMapper.login( userid, passwd );
		UserVo userVo = userMapper.login( userid, passwd );
		
		HttpSession session = request.getSession();
		session.setAttribute("login", userVo );
		session.setMaxInactiveInterval(30 * 60);	// 30분 동안 유지
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/");
		
		return mv;
		
	}
}
