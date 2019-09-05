
<%--@elvariable id="q" type="java.lang.String"--%>
<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo"--%>
<%--@elvariable id="oneType" type="com.worldkey.entity.OneType"--%>

<%--@elvariable id="threeTypes" type="java.util.List"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>展示管理页面</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<div>
		<form style="float: right;" class="form-inline" id="selectform"
			action="ShortVideo/videoRecycle">
			<input type="hidden" name="page" value="${pageinfo.pageNum}"
				id="page"> <input type="hidden" name="pagesize"
				value="${pageinfo.pageSize}" id="pagesize">
		</form>
	</div>

	<form style="float: right;" class="form-inline">
		<input type="text" placeholder="search" value="${q}" name="q" id="q"
			class="form-control"
			style="width: 250px; line-height: 30px; margin-top: 5px;">
		<button type="submit" class="btn btn-info"
			style="display: inline-block; margin-top: 5px;">搜索</button>
	</form>


	<table class="table table-hover table-striped text-center">
		<tr>
			<td>*</td>
			<td>标题</td>
			<td>操作</td>
			<td>链接</td>
			<td>刪除</td>
		</tr>
		<c:forEach items="${pageinfo.list}" var="item" varStatus="c">
			<tr>
				<td>${c.count}</td>
				<td>
					<%-- 没有标题的展示 --%> <c:if test="${item.name==null}">
						<a href="${item.url}">无标题${item.id}</a>
					</c:if> <%-- 有标题的展示 --%> <c:if test="${item.name!=null}">
						<a href="${item.url}">${item.name}</a>
					</c:if>
				</td>
				<td><select id="${item.id}" class="checked" title="">
						<!-- 已审核 -->
						<c:if test="${item.kf==1}">
							<option value="1" selected="selected">已审核</option>
							<option value="0">未审核</option>
						</c:if>
						<!-- 未审核 -->
						<c:if test="${item.kf==0}">
							<option value="1">已审核</option>
							<option value="0" selected="selected">未审核</option>
						</c:if>
				</select>
				</td>

				<td>${item.url}</td>
				<td>
					<button data-id="${item.id}"
						class="del btn-sm glyphicon glyphicon-trash btn btn-danger"></button>
							<input type="button" id="stick" data-id="${item.id}" class="btn btn-warning stick" value="恢復"  /> 
				</td>
			</tr>
		</c:forEach>
	</table>
	<!-- 分页导航结束 -->
	<%--推送的拟态框--%>
	<%@include file="../fragment/page.jsp"%>
	<script type="text/javascript">
$(function(){
var q="${q}";
$(".page").click(function () {
    var num = "${pageinfo.pageNum}";
    var c = $(this).attr("id");
    if (c === num) {
        return;
    }
    $('#page').attr("value", c);
    $('#selectform').submit();
});

$(".checked").change(function () {
    var checked = $(this).val();
    $.post('<%=basePath%>ShortVideo/checked?id=' + $(this).attr('id') + '&checked=' + checked, function (date) {
        alert(date.msg);
    })
});

$('#stick').click(function(){
	var id = $(this).data("id");
	$.ajax({
		url:"/ShortVideo/stick",
		type:'POST',
		async:'true',
		cache:"false",
		data:{id:id},
		success: function (data) {
			var code = data.code;
			if (code === 200) {
				alert("恢復成功");
				location.reload();
			}else{
				alert("未知错误!");
			}
        }
	});
});




    /*删除*/
    $(".del").click(function () {
        var id = $(this).data("id");
  //      if (window.confirm("此操作不可逆，確認刪除？")) {
            var obj = $(this).parent().parent();
            $.post("/ShortVideo/del", {id: id}, function (data) {
                if (data.code === 200 && data.msg === 'ok') {
                    obj.remove();
                    alert("");
                } else {
                    alert("删除失败");
                }
            })
   //     }
    });
})
</script>
</body>
</html>
