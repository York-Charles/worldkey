<%--
  Created by IntelliJ IDEA.
  User: DB112
  Date: 2017/11/24
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8"/>
    <title>websocket 广播式</title>
    <script src="js/jquery-3.1.1.min.js"></script>
    <script src="js/webSocket/sockjs.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/webSocket/stomp.js" type="text/javascript" charset="utf-8"></script>

</head>
<body>
<noscript><h2 style="color: #e80b0a;">Sorry，浏览器不支持WebSocket</h2></noscript>
<div>
    <div>
        <button id="connect" onclick="connect();">连接</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">断开连接</button>
    </div>

    <div id="conversationDiv">
        <label>输入通知的信息</label><input type="text" id="name" title=""/>
        <button id="sendName" onclick="sendName();">发送</button>
        <p id="response"></p>
    </div>
</div>
<script type="text/javascript">
    <!-- 定义stomp客户端 -->
    function setConnected(connected) {
        document.getElementById("connect").disabled = connected;
        document.getElementById("disconnect").disabled = !connected;
        document.getElementById("conversationDiv").style.visibility = connected ? 'visible' : 'hidden';
        $("#response").html();
    }

    var stompClient = null;

    function connect() {
        var socket = new SockJS('/endpointSang');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected:' + frame);
            stompClient.subscribe('/topic/getResponse', function (response) {
                showResponse(JSON.parse(response.body).message);
            })
        });

    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log('Disconnected');
    }

    window.onunload = function () {
        disconnect();
        alert("Bye now!");
    };

    function sendName() {
        var message = $('#name').val();
        console.log('message:' + message);
        stompClient.send("/systemNotice", {}, JSON.stringify({'message': message}));
    }

    function showResponse(message) {
        $("#response").html(message);
    }

     $(function () {
         $('#connect').click();
     })
</script>
</body>

</html>
