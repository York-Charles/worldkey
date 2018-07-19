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
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<title>Insert title here</title>
</head>
<body>
	<div class="input">
		<p>
			<a href="power/role/add" target="new" class="add">添加<img alt=""
				src="icon/icon/icon_btn_add.png">
			</a> <a href="action/list" target="new" class="add">全部权限</a>
		</p>
	</div>

	<table id="tb">
		<tr>
			<td>#</td>
			<td>名称</td>
			<td>##</td>
		</tr>
		<c:forEach items="${list}" var="c" varStatus="co">
			<tr>
				<td>${co.count}</td>
				<td>${c.name}</td>
				<td>${c.value}</td>
				<td><a href="power/action/${c.id}">查看权限</a> <a
					href="power/role/addaction?roleid=${c.id}">添加权限</a> <span
					class="compile" id="${c.id}"> <img alt=""
						src="icon/icon/icon_btn_write.png">
				</span> <span class="del" id="${c.id}"> <img alt=""
						src="icon/icon/icon_btn_cancel.png">
				</span></td>
			</tr>
		</c:forEach>
	</table>
</body>
<script>
		$(function(){
			$(".del").click(function() {
				if(window.confirm("确定删除?")){
				var obj=$(this).parent().parent();
				$.post("<%=basePath%>power/role/del/"+$(this).attr("id"), function(data){
   					if(data.msg=="ok"){
   					obj.remove();
   					alert("删除成功");
   					}else{
   					alert(data.result);
   					}
 					});
				}
				});
			$(".compile").bind("click",function(){
				if (window.confirm("确定修改?")) {
					location.replace("<%=basePath%>power/role/update?id="+ $(this).attr("id"));
					}
				});
	})
</script>
</body>
</html>