<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<title>Insert title here</title>
  <script src="//code.jquery.com/jquery-1.9.1.js"></script>
<style type="text/css">
input{
	padding-left:5px;
	margin-bottom:10px;
	font-size: 14px;
	border: 2px solid #dbb769;
	height: 30px;
	line-height: 30px;
	border-radius: 5px;
}

</style>
<script>
  </script>

</head>
<body>
<div>
<form action="admin/add" method="post">
<input name="name" placeholder="用户名"><br>
<input type="password" name="password" placeholder="密码"><br>
<input name="petname" placeholder="昵称"><br>
<input type="submit" value="添加">
</form>
</div>

</body>
</html>