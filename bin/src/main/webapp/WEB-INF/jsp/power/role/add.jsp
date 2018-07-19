<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--这里引用wangEditor.min.css 设置富文本的布局-->
<link rel="stylesheet" type="text/css" href="css/wangEditor.min.css">
<title>角色添加</title>
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<style type="text/css">
input[type=submit]{
	height: 30px;
	background-color: red;
	border-radius: 7px;
	margin-left: 80px;
}
</style>
</head>
<body>
	<form action="power/role/add" method="post">
		<table>
			<tr>
				<td>名称:</td>
				<td><input type="text" name="name" placeholder="名称" /></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;"><input
					type="submit" id="" name="" value="确定" /></td>
			</tr>
		</table>
	</form>
</body>
</html>