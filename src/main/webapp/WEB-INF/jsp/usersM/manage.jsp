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
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv=Content-Type content="text/html;charset=utf-8">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<meta content=always name=referrer>
    <title>世界钥匙管理平台</title>
    <script src="js/jquery-3.1.1.min.js"></script>
    <script src="js/webSocket/sockjs.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/webSocket/stomp.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        /*控制页面不会在iFrame框架中打开只会在浏览器顶端窗体打开*/
        if (top.location.href !== this.location.href) {
            top.location.href = this.location.href;
        }
        $(function () {
            <!-- 定义stomp客户端    -->
            var stompClient = null;
            function connect() {
                var socket = new SockJS('/endpointSang');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function (frame) {
                    console.log('Connected:' + frame);
                    stompClient.subscribe('/topic/getResponse', function (response) {
                        showResponse(JSON.parse(response.body).message);
                    })
                });
            }
            connect();
            function disConnect() {
                if (stompClient !== null) {
                    stompClient.disconnect();
                }
                console.log('Disconnected');
            }
            function showResponse(message) {
                alert(message);
            }
            window.onunload = disConnect;
        })

    </script>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <style type="text/css">
        body {
            font-size: 18px;
        }

        #new {
            height: 700px;
            width: 100%;
            border-radius: 20px;
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
            <h3 class="text-warning text-center">世界钥匙管理平台</h3>
        </div>
        <div class="col-sm-10">
            <h4 class="text-warning text-center">欢迎：${users.petName}</h4>
        </div>
    </div>

</div>
<div class="container bg-info">
    <div class="row">
        <div class="col-sm-2">
            <a href="/users/information/list" class="btn btn-default btn-block " target="new">已发布文章</a>
          
           <a href="/users/information/caogaoxiang" class="btn btn-default btn-block" >草稿箱</a>
       
        </div>
 
        <div class="col-sm-10">
            <iframe id="new" frameborder="0" name="new" src="/users/information/list"></iframe>

        </div>
               
    </div>
    
    
</div>


<script type="text/javascript">
    $(function () {

        $(".col-sm-2 a").click(function () {
            $(".col-sm-2 a").removeClass("btn-info");
            $(this).addClass("btn-info");
        });
    })
</script>
</body>
</html>