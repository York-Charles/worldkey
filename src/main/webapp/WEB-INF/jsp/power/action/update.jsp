<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
<!--这里引用wangEditor.min.css 设置富文本的布局-->
<link rel="stylesheet" type="text/css" href="css/wangEditor.min.css">
<link rel="stylesheet" type="text/css" href="css/wangEditorAdd.css">
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<style type="text/css">

input {
	padding-left: 5px;
}

</style>
</head>
<body>
 <%-- <form action="action/update" method="post">
 		<input type="hidden" name="id" value="${info.id }">
	 	<input type="text" value="${info.name }"  name="name"  placeholder="名称"/><br>
	 	<input type="text"value="${info.url }"  name="url" placeholder="相对链接"/><br>
	 <label>菜单：</label>	<select name="bar">
	 <c:forEach items="${barlist }" var="b">
	 <c:if test="${info.bar==b.id}" var="test">
	 <option value="${b.id}" selected="selected">${b.name}</option>
	 </c:if>"
	 
	 <c:if test="${not test}">
	 <option value="${b.id}">${b.name}</option>
	 </c:if>
	 </c:forEach>
	 	</select><br>
	 	<input type="submit" id="" name="" value="确定" />
	 </form>    --%>
	 <form:form action="action/update" method="post" modelAttribute="info">
	 <form:hidden path="id" />
	 <form:input path="name" placeholder="名称"/><br>
	 <form:input path="url" placeholder="url"/><br>
	 <input type="submit" value="保存" />
	 </form:form>
	 
	 
</body>
</html>