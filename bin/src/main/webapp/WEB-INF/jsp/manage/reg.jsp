<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <title>注册</title>
</head>
<body>
<form action="/member/reg" method="post">
    用户名：<input type="text" name="loginName"  maxlength="12" title=""><br>
    用户名：<input type="text" name="petName"  maxlength="12" title=""><br>
    密 码 ：<input type="password" name="password" maxlength="12" title=""><br>
   推荐码： <input type="text" name="recommendedCode" maxlength="12" title=""><br>
    <button type="submit" class="btn">注册</button>
</form>
</body>
</html>