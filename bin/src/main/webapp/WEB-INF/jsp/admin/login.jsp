<%--
  Created by IntelliJ IDEA.
  User: bubai_li
  Date: 2017/11/16
  Time: 18:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <title>管理员登陆</title>
    <style type="text/css">
        body{
            margin: 10% auto;
            width: 500px;
        }
        .group{
            border: 1px solid #666666;
            border-radius: 5px;
            height: 50px;
            padding: 0;
        }
        label{
            margin: 0;
            text-align: center;
            display: inline-block;
            height: 90%;
            width: 18%;

        }
        .input{
            display: inline-block;
            border: 0;
            height: 100%;
            width: 80%;
            border-left: 1px solid #666666;
        }
        .btn{
            margin-top: 10px;
            width: 80%;
            margin-left: 10%;
            height: 40px;
            border-radius: 10px;
            font-size: 30px;
            color:  #1e88e5;
            border: 0;
        }
    </style>
</head>
<body>

<form action="${pageContext.request.contextPath}/admin/login" method="post">
    <div class="group">
        <label for="name">账号</label>
        <input class="input" id="name" name="name" placeholder="账号" title="">
    </div>
    <div class="group">
        <label for="password">密码</label>
        <input type="password" class="input" placeholder="密码" id="password" name="password"><br>
    </div>
        <button class="btn" type="submit">登陆</button>
</form>


</body>
</html>
