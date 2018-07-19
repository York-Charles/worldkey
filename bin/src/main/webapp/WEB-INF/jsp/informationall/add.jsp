<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <!--这里引用wangEditor.min.css 设置富文本的布局-->
    <link rel="stylesheet" type="text/css" href="css/wangEditor.min.css">
    <link rel="stylesheet" type="text/css" href="css/wangEditorAdd.css">
    <link rel="stylesheet" type="text/css" href="css/manage1.css">
    <title>资讯添加</title>
</head>
<body>
<form enctype="multipart/form-data" method="post" action="informationall/add">
    <input type="hidden" name="checked" value="0">
    <div>
        <span>标题：</span><input type="text" id="title" name="title"/> <label>来源：</label><input
            type="text" id="auther" name="auther"/> <label>标题图片：</label><input
            type="file" name="file"/> <br> <label>摘要：</label><input
            type="text" name="abstracte"/> <br> <label>类别：</label> <select
            id="onetype">
        <c:forEach items="${onetypes}" var="c">
            <option value="${c.id}">${c.typeName}</option>
        </c:forEach>
    </select> <select name="type" id="twotype">
        <c:forEach items="${twotypes}" var="c">
            <option value="${c.id}">${c.typeName}</option>
        </c:forEach>
    </select> <input type="image" src="icon/icon/btn_save.png" alt="上传" id="upload" name=""
                     value="上传" class="add"/>
    </div>
    <textarea id="textarea1" name="info" rows="60" cols="20">
    请输入内容...
</textarea>
</form>

<!--引入jquery和wangEditor.js-->
<!--注意：javascript必须放在body最后，否则可能会出现问题-->
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="js/wangEditor.js"></script>
<script type="text/javascript">
    $(function () {
        $("#onetype").change(function () {
            var one = $(this).val();
            $.post("type/two/" + $(this).val(), function (date) {
                var two = "";
                for (var i = 0; i < date.result.length; i++) {
                    two += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
                }
                $("#twotype").html(two);
            })
        });

    })
</script>
<script type="text/javascript" src="js/editor.js"></script>
</body>
</html>