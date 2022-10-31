<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>房地产用户管理系统</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/css/style.css" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/css/public.css" />
</head>
<body>
<!--头部-->
    <header class="publicHeader">
        <h1>房地产用户管理系统</h1>
        <div class="publicHeaderR">
            <p><span>下午好！</span><span style="color: #fff21b"> ${userSession.userName }</span> , 欢迎你！</p>
            <a href="${pageContext.request.contextPath }/jsp/logout.do">退出</a>
        </div>
    </header>
<!--时间-->
    <section class="publicTime">
        <span>当前登录时间：${sessionScope.currentTime } &nbsp;&nbsp;&nbsp;  </span>
        <span>  上次登录时间：${sessionScope.lastAccessTime }</span>
    </section>
 <!--主体内容-->
 <section class="publicMian ">
     <div class="left">
         <h2 class="leftH2"><span class="span1"></span>功能列表 <span></span></h2>
         <nav>
             <ul class="list">

              <%--<li><a href="${pageContext.request.contextPath }/jsp/department.do?method=query">部门管理</a></li>--%>
                  <li ><a href="${pageContext.request.contextPath }/jsp/unit.do?method=queryUnit">单位管理</a></li>
                  <li><a href="${pageContext.request.contextPath }/jsp/user.do?method=query">用户管理</a></li>
                  <li><a href="${pageContext.request.contextPath }/jsp/log.do?method=logQuery">登录日志</a></li>
                  <li><a href="${pageContext.request.contextPath }/jsp/opLog.do">操作日志</a></li>
                  <li><a href="${pageContext.request.contextPath }/jsp/logout.do">退出系统</a></li>
             </ul>
         </nav>
     </div>
     <input type="hidden" id="path" name="path" value="${pageContext.request.contextPath }"/>
     <input type="hidden" id="referer" name="referer" value="<%=request.getHeader("Referer")%>"/>