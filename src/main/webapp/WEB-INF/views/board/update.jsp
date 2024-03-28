<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" type="image/png" href="/img/favicon.png" />
<link rel="stylesheet"  href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" />
<link rel="stylesheet"  href="/css/common.css" />

<style>
      
   #table {
   
   width: 800px;
   margin-bottom: 100px;
   
   td {
   text-align: center;
   padding: 10px;
   
   input[text] { width: 100%; }
   textarea { width: 100%; height: 250px; }
   
   &:nth-of-type(1) { width : 150px; background-color: black; color: white; }
   &:nth-of-type(2) { width : 250px; }
   &:nth-of-type(3) { width : 150px; background-color: black; color: white; }
   &:nth-of-type(4) { width : 250px; }
   
   }
   
   tr:nth-of-type(3) td:nth-of-type(2) {
   		text-align: left;
   }
   tr:nth-of-type(4) td[colspan] {
           height : 250px;
           width  : 600px; 
           text-align: left;
           vertical-align: baseline;  
    }
   tr:last-child td {
           background-color : white;
           color            : black;   
    }

   }
   
   textarea  {
      height: 250px;
      width : 100%;
   }

</style>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/browser-scss@1.0.3/dist/browser-scss.min.js"></script>

</head>
<body>
  <main>
  
    <%@include file="/WEB-INF/include/menus.jsp" %>		<!-- Controller(reqmap(/UpdateForm)) 에 menuList 를 넘겨주어야 화면에 메뉴가 출력된다 -->
  
	<h2>게시글 내용 수정</h2>
	<form action="/Board/Update" method="POST">		<!-- Update : mapper 이용해서 db 에 저장, 이후 list 로 돌아가야함 -->
	<input type="hidden" name="bno" value="${ vo.bno }" />		<!-- model 에 있는 bno : ${ vo.bno } -->
	<input type="hidden" name="menu_id" value="${ vo.menu_id }" />
	<table id="table">
	 <tr>
	   <td>글번호</td>
	   <td>${ vo.bno }</td>
	   <td>조회수</td>
	   <td>${ vo.hit }</td>
	 </tr>
	 <tr>
	   <td>작성자</td>
	   <td>${ vo.writer }</td>
	   <td>작성일</td>
	   <td>${ vo.regdate }</td>
	 </tr>
	 <tr>
	   <td>제목</td>
	   <td colspan="3"><input type="text" name="title" value="${ vo.title }" /></td>
	 </tr>
	 <tr>
	   <td>내용</td>
	   <td colspan="3"><textarea name="content">${ vo.content }</textarea></td>
	 </tr>
	 <tr>
	   <td colspan="4">
	    <input class="btn btn-primary btn-sm" type="submit" value="수정" />
	    <a class="btn btn-primary btn-sm" href="/Board/List?menu_id=${ vo.menu_id }">목록</a>
	   </td>
	 </tr>
	
	</table>	
	</form>   
	
  </main>
  
  <script>
  	const  goListEl  = document.getElementById('goList');
  	goListEl.addEventListener('click', function(e) {
  		location.href = '/Board/List';
  	})
  
  </script>
  
</body>
</html>





