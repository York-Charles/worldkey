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
<meta http-equiv=Content-Type content="text/html;charset=utf-8">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<meta content=always name=referrer>
<title>后台管理系统登录</title>
	<script src="/js/jquery-3.1.1.min.js" type="text/javascript"></script>
</head>
<script type="text/javascript">
        if (top.location.href !== location.href) {
            top.location.href = location.href;
        }
</script>
<style type="text/css">

	*{
		margin: 0;
		padding: 0;
	}
	body{
		width: 100%;
		height:auto;
		background: url("icon/bg.jpg") no-repeat center;
		background-size:100% 100%;
		overflow: hidden;
		margin-left: -50px;
		margin-bottom: -50px;
	}
	.header{
		height: 82px;
		font-size: 18px;
		color: #ffffff;
		text-align: center;
		line-height: 250px;
		margin-top: -142px;
		margin-left: 50px;
	}
	
	.content{
		  width: 33%;
		height: 325px;
		margin-left: 900px;
		margin-top: 200px;
		border-radius: 5px;
			background: url("icon/modular_bg.png") no-repeat center;
	}
	#meue ul{
		list-style: none;
		height: 50px;
		line-height: 50px;
	}
	#meue ul li{
		width: 244px;
		height: 60px;
		line-height: 60px;
		float: left;
		text-align: center;
		color: #ddd;
		cursor: pointer;
		font-size: 16px;
		border:1px solid #a8d5ff;
		border-left: none;
		border-top: none;
		border-top: #666;

	}
	#show{
		clear: both;
		line-height: 24px;
	}
	#show{
		width: 490px;
		height: 236px;
	}
	#meue ul li.bg{
		font-size: 18px;
		color: #fff;

	}
	#show div{
		width: 100px;
		height: 200px;
		float: left;
		display: none;
	}
	.yonghu{
		width:210px;
		height:42px;
		border-radius: 20px;
		background:transparent;
		outline: none;
		margin-top: -30px;
		border:2px solid #a8d5ff;
		color: #fff;
		font-size:14px;
		padding-left:78px;
		background: url("/icon/User.png") no-repeat 5px center;
		margin-left:100px;
	}
	.pws{
		width:210px;
		height:42px;
		border-radius: 20px;
		background:transparent;
		outline: none;
		margin-top: 15px;
		border:2px solid #a8d5ff;
		color: #fff;
		font-size:14px;
		padding-left:78px;
		background: url("icon/Lockeed.png") no-repeat 5px center;
		margin-left:100px;
	}
	.btn{
		width:176px;
		height:40px;
		display: block;
		background:#8abd69;
		border-radius: 10px;
		text-align: center;
		line-height: 40px;
		color: #ffffff;
		font-weight:bolder;
		font-size: 20px;
		margin-top: 24px;
		margin-left: 160px;
	}
	.title{
	height: 82px;
		font-size: 25px;
		color: #ffffff;
		white-space:nowrap;
		text-align: center;
		line-height: 150px;
		margin-left: 140px;
	}
	.imgs{
	 margin-left: 300px;
	 margin-top: 100px;
	 
	}
</style>
<body>
<div id="wapper">
	

	<div id="container">
		<div class="content">
			<div id="show">
				<div  style="display:block" id="usersform">
			        <span class="title">世界钥匙管理系统</span>
					<input type="text" id="loginName" class="yonghu" maxlength="12" title="">
					<input type="password" id="password" class="pws" maxlength="12" title="">
					<button type="button" class="btn">登录</button>
					<%--<a href="/member/regHtml" class="btn">注册</a>--%>
				</div>
			</div>
			<img src="./icon/Gear.png" class="imgs">
			<div class="header">
    <h3 style="float: right"><a href="/admin/toLogin">管理員入口</a> </h3>
	</div>
		</div>
		<div style="margin: 150px 200px 150px 700px;color: #FFFFFF;">京ICP备16011251号-2</div>
	</div>
</div>
<script type="text/javascript">
	$(function () {
		$('.btn').click(function () {
			var loginName=$('#loginName').val();
			var password=$('#password').val();
			$.post('users/dologin?loginName='+loginName+'&password='+password,function (data) {
			    if (data==='ok'){
			        location.href='/users/manage';
				}else{
				alert(data);
                }
            });
        });
        $(window).keydown(function(event){
            if (event.keyCode===13){
                $('.btn').click();
			}
            /*switch(event.keyCode) {
                // ...
                // 不同的按键可以做不同的事情
                // 不同的浏览器的keycode不同
                // 更多详细信息:     http://unixpapa.com/js/key.html
                // 常用keyCode： 空格 32   Enter 13   ESC 27
            }*/
        });
	});





</script>

</body>
</html>