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
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.9.1.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script>

  </script>
</head>
<body>

<div>
<form action="admin/update" method="post">
<input type="hidden" name="id" value="${vo.id}">
<input name="name" placeholder="用户名" value="${vo.name }"><br>
<input name="petname" placeholder="昵称"value="${vo.petname }"><br>
<input type="password" name="password" value="${vo.password }" placeholder="密码"><br>
<input type="submit" value="修改">
</form>
</div>
</body>
</html>