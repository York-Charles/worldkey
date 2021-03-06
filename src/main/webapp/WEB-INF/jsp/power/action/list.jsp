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
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<title>权限列表</title>
</head>
<body>
		<div class="input">
			<p><a href="action/add" target="new" class="add">
	 添加<img alt="" src="icon/icon/icon_btn_add.png">
		</a></p>
		</div>
		<c:if test="${list.size()!=0 }" var="id1">
		<table id="tb">
			<tr>
				<td style="width:100px">序号</td>
				<td style="width:100px">名称</td>
				<td style="width:200px">链接</td>
				<td style="width:150px">操纵</td>
			</tr>
			<c:forEach items="${list }" var="c" varStatus="co">
			<tr>
				
				<td>${co.count}</td>
				<td>${c.name}</td>
				<td>${c.url}</td>
				<td>
					<span class="compile" id="${c.id}">
				<img alt="" src="icon/icon/icon_btn_write.png"></span>
					<span class="del" id="${c.id}">
					<img alt=""src="icon/icon/icon_btn_cancel.png"></span>
				</td>
			</tr>
			</c:forEach>
		</table>
		</c:if>
		<c:if test="${list.size()==0}">
				<td colspan="4">该角色没有任何权限</td>
				</c:if>
	</body>
	<script>
		$(function(){
			$(".del").click(function() {
				if(window.confirm("确定删除?")){
				var obj=$(this).parent().parent();
				$.post("action/del/"+$(this).attr("id"), function(data){
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
					location.replace("/action/update?id="+$(this).attr("id"));
				}
			});
		})
	</script>
</body>
</html>