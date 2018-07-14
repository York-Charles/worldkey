<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <title></title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }
    </style>
</head>

<body style="margin: 0; padding: 0;">
<div style="position: relative;">
    <h1 style="position: absolute; top: 14%;font-size: 36px;color: #333;margin: 0 auto;width: 100%;text-align: center;">${petName}</h1>
    <img src="/img/beinvited_bg.png" style="width: 100%; height: 100%;" />
    <div style="position: absolute; top: 41%; z-index: 10000;">
        <img src="/img/beinvited_bg_white.png" style="width: 70%;margin-left: 15%; height: 32%" />
        <h1 style="font-size: 30px;position: absolute;top: 29%;width: 100%;text-align: center;color: #fc4e4e;">${loginName}</h1>
    </div>
    <div style="position: absolute;bottom: 10%;text-align: center;width: 100%;">
        <img src="/img/btn.png" style="width: 48%;" id="btn" />
    </div>
</div>
<script type="text/javascript">
    document.getElementById("btn").onclick = function(){
        window.location.href="                                                                                                                                            /image/app-release.apk";
    }
</script>
</body>

</html>
