<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<title>Insert title here</title>
</head>
<body>
<input type="hidden" id="admin" value="${id}">
		<div class="input">
			<p>
				<a href="admin/role/add?admin=${id}" target="new" class="add">
				<span>添加</span><img alt="" src="icon/icon/icon_btn_add.png"> </a>
			</p>
		</div>
		<table id="tb">
			<tr>
				<td style="width:100px">序号</td>
				<td style="width:100px">名称</td>
				<td>操纵</td>
			</tr>
			<c:forEach items="${list }" var="c" varStatus="co">
			<tr>
				<td>${co.count}</td>
				<td>${c.name}</td>
				<td>
					<span class="del" id="${c.id}"><img alt="" src="icon/icon/icon_btn_cancel.png"></span> 
				</td>
			</tr>
			</c:forEach>
		</table>
	</body>
	<script>
		$(function(){
			$(".del").click(function() {
				if(window.confirm("确定删除?")){
				var obj=$(this).parent().parent();
				$.post("<%=basePath%>admin/role/del?role="+$(this).attr("id")+"&admin="+$("#admin").val(), function(data){
   					if(data.msg=="ok"){
   					obj.remove();
   					alert("删除成功");
   					}else{
   					alert(data.result);
   					}
 					});
				}
				});
		});
	</script>
</body>
</html>