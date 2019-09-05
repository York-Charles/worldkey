<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2017/10/23
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>资讯</title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="/js/jquery-3.1.1.min.js"></script>
    <title>用户查看</title>
</head>
<body>
<shiro:hasRole name="usersManage">
<a class="btn btn-info" href="/users/getExcel">导出全部用户列表</a>
</shiro:hasRole>
<form action="/users/list" method="get" id="form">
    <input type="hidden" id="pageNum" name="pageNum" value="1">
    <input type="hidden" name="pageSize" value="10">
</form>
<table border="1" cellspacing="0" cellpadding="1">
    <tr>
        <th>#</th>
        <th>用户名</th>
        <th>昵称</th>
        <th>性别</th>
        <th>生日</th>
        <th>头像</th>
        <th>联系方式</th>
        <th>余额</th>
        <th>注册时间</th>
    </tr>
    <c:forEach items="${pageinfo.list}" var="item" varStatus="s">
    <tr>
        <td>${s.count}</td>
        <td>${item.loginName}</td>
        <td>${item.petName}</td>
        <td>${item.sex==1?"男":"女"}</td>
        <td>${item.birthday}</td>
        <td>
            <img src="${item.headImg}" width="30" height="30">
        </td>
        <td>${item.telNum}</td>
        <td>${item.balance}</td>
        <td>${item.createDate}</td>
    </tr>
    </c:forEach>
</table>
<%@include file="../fragment/page.jsp"%>

<script type="text/javascript">
    $(".page").click(function () {
        var num = "${pageinfo.pageNum}";
        var c = $(this).attr("id");
        if (c === num) {
            return;
        }
        $('#pageNum').val(c);
        $('#form').submit();
    });
</script>
</body>
</html>
