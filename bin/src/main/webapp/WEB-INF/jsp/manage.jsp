<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>世界钥匙管理系统</title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet"
          href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <script src="js/jquery-3.1.1.min.js"></script>
    <style type="text/css">
        body {
            font-size: 18px;
        }

        #new {
           height: 700px;
            width: 100%;
        }

        .btn {
            outline: none !important;
        }

        .container {
            width: 100% !important;
        }
    </style>

</head>
<body class="bg-info">
<div class="container bg-info">
    <div class="row">
        <div class="col-sm-2">
            <div>
                <h4 class="text-warning text-center">欢迎:${admin.petname}</h4>
            </div>
            <shiro:hasPermission name="informationall:list">
                <a class="btn btn-default btn-block" href="informationall/list"
                   target="new">资讯管理</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="power:roles">
                <a class="btn btn-default btn-block" href="power/roles"
                   target="new">权限管理</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="admin:list">
                <a class="btn btn-default btn-block" href="admin/list" target="new">管理员管理</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="type:list">
                <a class="btn btn-default btn-block" href="type/one" target="new">模块管理</a>
            </shiro:hasPermission>
            <shiro:hasRole name="administrator">
                <a class="btn btn-default btn-block" href="system/update" target="new">系统管理</a>
            </shiro:hasRole>
            <shiro:hasRole name="sensitiveWord">
                <a class="btn btn-default btn-block" href="sensitiveWord/showWord" target="new">敏感词管理</a>
            </shiro:hasRole>
            <shiro:hasRole name="usersManage">
                <a class="btn btn-default btn-block" href="users/list" target="new">用户管理</a>
            </shiro:hasRole>
                <a class="btn btn-default btn-block" href="show/manageList?oneType=25" target="new">文章</a>
                <a class="btn btn-default btn-block" href="show/manageList?oneType=26" target="new">闲置</a>
                <!--  <a class="btn btn-default btn-block" href="show/manageList?oneType=27" target="new">俱乐部</a>  -->
               <a class="btn btn-default btn-block" href="show/manageList?oneType=33" target="new">说说</a>
                <a class="btn btn-default btn-block" href="system/tongZhi" target="new">推送通知</a> 
               
        </div>
      
        <div class="col-sm-10">
            <iframe id="new" frameborder="0" name="new" src="manage/welcome"></iframe>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $("a").click(function () {
            $("a").removeClass("btn-info");
            $(this).addClass("btn-info");
        })
    })
</script>
</body>
</html>