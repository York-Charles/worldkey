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
<!-- <style> 
			table{
				border-collapse:collapse;
				margin:35px 0 0 15px;
			}
			table tr td{
				height:25px; 
				border:1px solid #000;
			    text-align:center;
			    
			}
			table tr td a{
				text-decoration:none;
				color:#000;
			}
			tr:hover{
				background:lightgreen;
				cursor:pointer;
			}
			div .first{
				width:120px;
				height:30px;
				outline:none;
				border:1px solid #999;
				padding-left:10px;
				margin-top:10px;		
			}
			div .first1{
				width:120px;
				height:30px;
				outline:none;
				border:1px solid #999;
				padding-left:10px;
				margin-top:10px;
				margin-left:10px;		
			}
			.add{
				text-decoration:none;
				padding:10px 0 10px 10px;
			}
			#pagebox{
			margin-left: 30%;
			margin-top: 10px;
			}
		</style> -->
<title>Insert title here</title>
</head>
<body>
		<input type="hidden" id="role" value="${roleid}">
		<table id="tb">
			<tr>
				<td style="width:100px">序号</td>
				<td style="width:100px">名称</td>
				<td style="width:200px">链接</td>
				<!-- <td style="width:150px">菜单</td> -->
				<td>操纵</td>
			</tr>
			<c:forEach items="${list }" var="c" varStatus="co">
			<tr>
				
				<td>${co.count}</td>
				<td>${c.name}</td>
				<td>${c.url}</td>
				<%-- <td>${c.barName}</td> --%>
				<td>
					<span class="del"  id="${c.id}">
					<img alt="" src="icon/icon/icon_btn_cancel.png">
					</span>
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
				$.post("power/role/delaction?action="+$(this).attr("id")+"&role="+$("#role").val(), function(data){
   					if(data.msg=="ok"){
   					obj.remove();
   					alert("删除成功");
   					}else{
   					alert(data.result);
   					}
 					});
				}
				});
		})
	</script>
</body>
</html>