<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
<script src="js/jquery-3.1.1.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.0.0.js"></script>
<link rel="stylesheet" type="text/css" href="css/manage1.css">
<style type="text/css">
.input {
display:block;
margin-bottom: 10px;

}
#addtype input{
text-align:center;
margin-bottom:10px;
	font-size: 14px;
	border: 2px solid #dbb769;
	height: 30px;
	line-height: 30px;
	border-radius: 5px;
	}
.name{
line-height: 28px;
border: 1px solid #ffffff;
font-size: 14px;
}
#addtype{
text-align:center;
display:none;
position: absolute;
left: 30%;
top: 40%;
}
body{
width:100%;
position: relative;
}
.add>img{
width: auto;
height: auto;
}
.add{
cursor: pointer;
}
</style>
<title>一级分类列表</title>
</head>
<body>
	<div class="input">
		<a target="new" class="add"> <span>添加</span><img
				alt="" src="icon/icon/icon_btn_add.png"></a>
	</div>
	<hr>
	<table id="tb">
		<tr>
			<td>序号</td>
			<td>名称</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${list }" var="c" varStatus="co">
			<tr>
				<td>${co.count}</td>
				<td>
				<input value="${c.typeName}" readonly="readonly" class="name"></td>
				<td>
					<span class="find"  id="${c.id}"><a href="type/two/list/${c.id}">分类</a></span>
					<span class="compile"  id="${c.id}"> 
						<img alt=""src="icon/icon/icon_btn_write.png">
					</span> 
					<span class="del" id="${c.id}">
					 <img alt=""src="icon/icon/icon_btn_cancel.png">
					</span>
				</td>
			</tr>
		</c:forEach>
	</table>
	
	<div id="addtype" class="input">
	<img alt="" id="hid" src="icon/icon/icon_btn_cancel.png">
	<form action="type/one/add" method="post" target="new">
	<input name="typeName" placeholder="模块名"><br>
	<input type="submit" value="添加">
	</form>
	</div>
</body>
<script>
		$(function(){
			$(".add").click(function(){
			$("#addtype").css("display","block");
			});
			$("#hid").click(function(){
			$("#addtype").css("display","none");
			});
			
			
			
			$("tr").mouseover(function(){
				$(this).children().eq(1).children("input").css("background-color","lightgreen");
			});
		  	$("tr").mouseout(function(){
				$(this).children().eq(1).children("input").css("background-color","#ffffff");
			});  
			$(".del").click(function() {
				if(window.confirm("确定删除?")){
				var obj=$(this).parent().parent();
				$.post(" <%=basePath%>type/one/del/"+$(this).attr("id"), function(data){
   					if(data.msg=="ok"){
   					obj.remove();
   					alert("删除成功");
   					}else{
   					alert(data.result);
   					}
 					});
				}
				});
			$(".compile").toggle(function(){
				$(this).children().attr("src","icon/icon/btn_save.png");
				$(this).parent().parent().children().eq(1).children("input").removeAttr("readonly");
				$(this).parent().parent().children().eq(1).children("input").css("border-color","#e3e3e3");
			},function(){
				if(window.confirm("确定修改?")) {
				var name=$(this).parent().prev().children("input").val();
					$.post("type/one/update?id="+$(this).attr('id')+"&typeName="+name, function(data){
	   					if(data.msg=="ok"){
	   					alert("保存成功");
	   					}else{
	   					alert(data.result);
	   					}
	 					});
			}
			$(this).children().attr("src","icon/icon/icon_btn_write.png");
			$(this).parent().parent().children().eq(1).children("input").attr("readonly","readonly");
			$(this).parent().parent().children().eq(1).children("input").css("border-color","#ffffff");
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
				var two="<option value=\"0\">不限</option>";
				for (var i = 0; i < date.result.length; i++) {
					two+="<option value="+date.result[i].id+">"+date.result[i].typeName+"</option>";
				}
				$("#twotype").html(two);
		});
		});
	</script>




</body>
</html>