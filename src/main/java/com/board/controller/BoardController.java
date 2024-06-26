package com.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.board.domain.BoardVo;
import com.board.mapper.BoardMapper;
import com.board.menus.domain.MenuVo;
import com.board.menus.mapper.MenuMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/Board")
public class BoardController {
	
	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	//  /Board/List?menu_id=MENU01
	@RequestMapping("/List")
	public ModelAndView list( MenuVo menuVo ) {
		
		log.info("=====================menuVo : {}", menuVo );		// 20240326 14:33:01.393 [http-nio-9090-exec-3] INFO c.b.c.BoardController - menuVo : MenuVo(menu_id=MENU01, menu_name=null, menu_seq=0)
																	// menu_name = null 인 이유 : ?menu_id=MENU01 만 넘어왔고 그것만 menuVo 에 담겨있기 때문
		
		// 메뉴 목록 조회
		List<MenuVo> menuList = menuMapper.getMenuList();
		
		// 게시물 목록 조회
		List<BoardVo> boardList = boardMapper.getBoardList( menuVo );	// boardList : db 에서 들고옴 -> boardMapper
		System.out.println( "boardList: " + boardList );
		
		MenuVo mVo = menuMapper.getMenu( menuVo.getMenu_id() );		// menuVo = line30 의 (MenuVo menuVo)
		String menu_id = mVo.getMenu_id();
		String menu_name = mVo.getMenu_name();						// menuVo 에서 getMenu_id 해서 조회(select) 한걸 mVo에 담고 거기서 getMenu_name 한걸 menu_name 에 담음
		
		ModelAndView mv = new ModelAndView();
		mv.addObject( "menu_id", menu_id );
		mv.addObject( "menu_name", menu_name );		
		mv.addObject( "menuList", menuList );
		mv.addObject( "boardList", boardList );
		mv.setViewName( "board/list" );
		
		return mv;
	}
	
	//  /Board/WriteForm
	@RequestMapping("/WriteForm")
	public ModelAndView writeForm( MenuVo menuVo ) {
		
		// 메뉴 목록 조회
		List<MenuVo> menuList = menuMapper.getMenuList();
		System.out.println( "[==MenuList==] : " + menuList );
		
		// 넘어온 menu_id(= ?menu_id=MENU01) 를 처리
		String menu_id = menuVo.getMenu_id();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("menuList", menuList);		// 앞의 "" 안 : jsp 에서 items=${} 할 때 쓰는 글자 곧 model 이름, 다르면 안됨 / 뒤 : List<MenuVo> menuList 의 menuList 곧 변수 이름
		mv.addObject("menu_id", menu_id);
		mv.setViewName("board/write");
		return mv;
		
	}
	
	//  /Board/Write
	@RequestMapping("/Write")
	public ModelAndView write( BoardVo boardVo ) {
		
		// 게시글 저장 -> mapper 에 insert
		// 넘어온 값 Board 저장
		boardMapper.insertBoard( boardVo );		// 내생각: 넘어온 값을 담은 boardVo 를 boardMapper.xml 안에 있는 id="insertBoard" SQL 문 실행(= insert)
		
		String        menu_id =  boardVo.getMenu_id();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/Board/List?menu_id=" + menu_id);
		return mv;
		
	}
	
	// 게시글 읽기 : /Board/View?bno=1
	@RequestMapping("/View")
	public ModelAndView view( BoardVo boardVo ) {
		
		// 메뉴목록 조회 필요
		List<MenuVo> menuList = menuMapper.getMenuList();
		
		// 조회수 증가 ( 현재 bno 의 HIT = HIT + 1 )
		boardMapper.incHit( boardVo );		// inclement : 증가
		
		// bno 로 조회한 게시글 정보 읽기
		BoardVo vo = boardMapper.getBoard( boardVo );		// Board : bno, title, content etc  ->  한줄짜리 정보를 다 담고 있는 칼럼
												// -> 메뉴별 bno=1 다 있음 -> 필요한 bno=1 들고오기 위해 menu_id(또는 menu_id 가 담겨있는 boardVo) 를 담아서 getView
		System.out.println( "===조회된 vo: " + vo );
		
		// vo.content 안의 '\n' (= enter) 을 '<br>' 로 변경
		String content = vo.getContent();
		if( content != null ) {
			content = content.replace("\n", "<br>");
			vo.setContent( content );	
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("menuList", menuList);
		mv.addObject("vo", vo);				// 여기 vo 는 넘어온 vo(= boardVo)랑 다르다, view.jsp 에서 여기 vo 안에 있는 bno 를 꺼내 쓸 것
		mv.setViewName("board/view");		// view.jsp 갈때 출력 필요, model 에 담아서 -> 윗줄 필요
		return mv;
		
	}
	
	// 삭제 : /Board/Delete?bno=3&menu_id=MENU01
	@RequestMapping("/Delete")
	public ModelAndView delete( BoardVo boardVo ) {		// bno 가지고 있는 애 : BoardVo
		
		// 게시글 삭제
		boardMapper.deleteBoard( boardVo );		// delete 함수는 (id="deleteBoard" 인 SQL문으로) boardVo 를 가지고 가서 삭제할 것이다
		
		String menu_id = boardVo.getMenu_id();
		
		// 다시 조회
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/Board/List?menu_id=" + menu_id);
		return mv;
		
	}
	
	// 수정 : /Board/UpdateForm?bno=5&menu_id=MENU08
	@RequestMapping("/UpdateForm")
	public ModelAndView updateForm( BoardVo boardVo ) {
		
		List<MenuVo> menuList = menuMapper.getMenuList();
		
		BoardVo vo = boardMapper.getBoard( boardVo );
		
		ModelAndView mv = new ModelAndView();
		mv.addObject( "vo", vo );
		mv.addObject( "menuList", menuList );
		mv.setViewName( "board/update" );		// update.jsp 를 찾아라
		return mv;
		
	}
	
	// 수정 : /Board/Update
	@RequestMapping("/Update")
	public ModelAndView update( BoardVo boardVo ) {
		
		// 수정
		boardMapper.updateBoard( boardVo );
		
		String menu_id = boardVo.getMenu_id();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/Board/List?menu_id=" + menu_id);
		return mv;
				
	}
	
	
	
}
