<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<base href="<%=basePath%>">
<title></title>
<meta name="keywords" content="keyword1,keyword2,keyword3" />
<meta name="description" content="this is my page" />
<meta name="content-type" content="text/html; charset=UTF-8" />
<!--这里引用wangEditor.min.css 设置富文本的布局-->
<link rel="stylesheet" type="text/css" href="css/wangEditor.min.css">
<link rel="stylesheet" type="text/css" href="css/wangEditorAdd.css">
</head>
<body>
 <form action="power/role/update" method="post">
 		<input type="hidden" name="id" value="${info.id }">
	 	<label>名称：</label><input type="text" value="${info.name }"  name="name"  placeholder="名称"/><br>
	 	<input type="submit" id="" name="" value="确定" />
	 </form>   
</body>
</html>