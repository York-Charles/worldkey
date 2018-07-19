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
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--这里引用wangEditor.min.css 设置富文本的布局-->
<link rel="stylesheet" type="text/css" href="css/wangEditor.min.css">
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<title>角色添加</title>
<style type="text/css">
input{
	padding-left:5px;
}
</style>
</head>
<body>
	<form action="admin/role/add" method="post">
	<input type="hidden" name="admin" value="${admin }">
		<table>
			<tr>
				<td>角色:</td>
				<td>
				<select name="role">
				<c:forEach items="${rolelist}" var="r">
				<option value="${r.id }">${r.name }</option>
				</c:forEach> 
				</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;"><input
					type="submit" id="" name="" value="确定" /></td>
			</tr>
		</table>
	</form>
</body>
</html>