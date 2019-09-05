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
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<title>资讯添加</title>
<style type="text/css">
input[type=submit], select {
	font-size: 14px;
	border: 2px solid #dbb769;
	height: 30px;
	line-height: 30px;
	border-radius: 5px;
}
</style>
</head>
<body>
	<form action="power/role/addaction" method="post">
		<table>
			<tr>
				<td>名称:</td>
				<td><input type="hidden" name="role" value="${info.id }"/>
					${info.name}
				</td>
			</tr>
			<tr>
				<td>权限：</td>
				<td><select name="action">
						<c:forEach items="${actionlist }" var="b">
							<option value="${b.id}">${b.name}</option>
						</c:forEach>
						<c:if test="${actionlist.size()==0 }">
							<option>没有更多了</option>
						</c:if>
				</select></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;"><input
					type="submit" id="" name="" value="添加" /></td>
			</tr>
		</table>
	</form>
</body>
</html>