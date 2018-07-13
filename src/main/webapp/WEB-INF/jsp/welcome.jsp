<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<title>Insert title here</title>
<style>
			.welcome{ 
				font-size: 30px;
				font-weight: 700; 
				padding-top: 225px;
				padding-left: 20%; 
				color: rgb(178,136,80);
			}
			body {
			width:800px;
			overflow:hidden;
			background-color: #ffffff;
}
		</style>
	</head>
	<body>
	<div class="welcome">欢迎来到"世界钥匙"管理系统</div>
	</body>
</html>