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
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script><link rel="stylesheet" href="http://www.w3cschool.cn/statics/plugins/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<style type="text/css">
input{
	padding-left:5px;
}
</style>
<title>Insert title here</title>
</head>
<body>
		<div class="input">
		<form action="admin/list" id="selectform">
			<label>用户名</label><input type="text" name="name" value="${select.name }" placeholder="用户名"> 
			<label>昵称</label><input type="text" name="petname" value="${select.petname }" placeholder="昵称"> 
			<input type="hidden" name="page" value="${pageinfo.pageNum}" id="page">
			<input type="hidden" name="pagesize" value="${pageinfo.pageSize}" id="pagesize">
			<input type="image" src="icon/icon/icon_btn_search.png" value="查找">
			<a href="admin/add" target="new" class="add"><span>添加</span><img
				alt="" src="icon/icon/icon_btn_add.png"></a>
			</form>
			
		</div>
		
		<table id="tb">
			<tr>
				<td>序号</td>
				<td>用户名</td>
				<td>昵称</td>
				<td>操作</td>
			</tr>
			<c:forEach items="${pageinfo.list }" var="c" varStatus="co">
			<tr>
				<td>${co.count}</td>
				<td> ${c.name}</td>
				<td>${c.petname}</td>
				<td>
					<span class="action" id="${c.id}">查看权限</span>
					<span class="del" id="${c.id}">删除</span>
					<span><a href="admin/update?id=${c.id}" target="new">更新</a></span>
					<span class="reloadpwd" id="${c.id}">重置密码</span>
				</td>
			</tr>
			</c:forEach>
		</table>
		<div id="pagebox">
		<c:forEach begin="1" end="${pageinfo.pages}" var="i">
		<c:if test="${pageinfo.pageNum==i }">
		<button class="page" id="${i}" disabled="disabled">${i}</button>
		</c:if>
		<c:if test="${pageinfo.pageNum!=i }">
		<button class="page" id="${i}">${i}</button>
		</c:if>
		</c:forEach>
		 </div>
	</body>
	<script>
		$(function(){
			$(".del").click(function() {
				if(window.confirm("确定删除?")){
				var obj=$(this).parent().parent();
				$.post(" <%=basePath%>admin/del?id="+$(this).attr("id"), function(data){
   					if(data.msg=="ok"){
   					obj.remove();
   					alert("删除成功");
   					}else{
   					alert("权限不足");
   					}
 					});
				}
				});
			$(".reloadpwd").click(function() {
				if(window.confirm("确定重置?")){
				var obj=$(this).parent().parent();
				$.post("<%=basePath%>admin/reloadpwd?id="+$(this).attr("id"), function(data){
   					if(data.msg=="ok"){
   					alert("成功");
   					}else{
   					alert("权限不足");
   					}
 					});
				}
				});
			$(".action").click(function() {
			location.replace("<%=basePath%>admin/roles?id="+$(this).attr("id"));
				});
			$(".page").click(function(){
				var c=$(this).attr("id");
				$("#page").attr("value",c);
				$("#selectform").submit();
			});
		});
		$("#onetype").change(function  () {
			var one=$(this).val();
			$.post("<%=basePath%>type/two/"+$(this).val(),function  (date) {
				var two="";
				for (var i = 0; i < date.result.length; i++) {
					two+="<option value="+date.result[i].id+">"+date.result[i].typeName+"</option>";
				}
				$("#twotype").html(two);
		});
		});
	</script>
</body>
</html>