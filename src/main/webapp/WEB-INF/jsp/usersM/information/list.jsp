<%@ page  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>世界钥匙管理系统</title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <!--导入jquery.js -->
    <script src="js/jquery-3.1.1.min.js" type="text/javascript"></script>
    <title>资源管理</title>
</head>
<body style="margin-top: 5px">
<%--@elvariable id="pageinfo" type="com.github.pagehelper.PageInfo"--%>
<%--@elvariable id="select" type="com.worldkey.entity.InformationExample"--%>
<sf:form action="users/information/list"
         class="form-inline text-center" id="selectform"
         modelAttribute="select">

    <div class="input-group">
        <span class="input-group-addon">筛选</span>
        <sf:input class="form-control" path="title" placeholder="标题"/>
    </div>
  <%--  <div class="input-group">
        <span class="input-group-addon">分类</span>
        <sf:select path="onetype" class="form-control" id="onetype"
                   items="${onetypes}" itemLabel="typeName" itemValue="id">
        </sf:select>
    </div>
    <label class="glyphicon glyphicon-menu-right"></label>
    <sf:select path="type" class="form-control" id="type"
               items="${twotypes}" itemLabel="typeName" itemValue="id">
    </sf:select>--%>
    <input type="hidden" name="page" value="${pageinfo.pageNum}" id="page">
    <input type="hidden" name="pagesize" value="${pageinfo.pageSize}"
           id="pagesize">
    <button class=" btn btn-sm btn-warning glyphicon glyphicon-search ">查询</button>
    <a href="users/information/writeArticle"
       class="btn btn-sm btn-warning  glyphicon glyphicon-plus" target="_blank">写文章</a>
 </sf:form>
 <c:if test="${pageinfo.list.size()==0 }">
     该结果没有任何信息
 </c:if>
 <c:if test="${pageinfo.list.size()!=0 }">
     <table id="tb" class="table table-hover text-center  table-condensed ">
         <thead>
         <tr>
             <th class="text-center">#</th>
             <th class="text-center">标题</th>
           <!--  <th class="text-center">类型</th>   --> 
             <th class="text-center">状态</th>
             <th class="text-center">#</th>
         </tr>
         </thead>
         <tbody>
         <c:forEach items="${pageinfo.list }" var="c" varStatus="co">
             <tr>
                 <td>${co.count}</td>
                 <td><a href="${c.weburl}" target="new">${c.title}</a></td>
                     <%--@elvariable id="type" type="java.util.List"--%>
                 <!--  <td><c:forEach items="${type}" var="one">
                        <c:forEach items="${one.twoTypes}" var="two">
                             <c:if test="${c.type==two.id}">
                                 ${one.typeName}(${two.typeName})
                             </c:if>  
                         </c:forEach> 
                     </c:forEach> </td>    -->  
                 <td>${c.checked==0?"未审核":(c.checked==1?"已审核":"已下架")}</td>
                 <td><c:if test="${c.checked==0}">
                     <button id="${c.id}"
                             class="compile btn-sm glyphicon glyphicon-edit btn btn-warning"></button>
                 </c:if>
                     <button id="${c.id}"
                             class="del btn-sm glyphicon glyphicon-trash btn btn-danger"></button>
                 </td>
             </tr>
         </c:forEach>
         </tbody>
     </table>
     <!-- 分页导航 -->
     <%@include file="../../fragment/page.jsp" %>
     <!-- 分页导航结束 -->
 </c:if>
 <script>
     $(function () {
         $(".del").click(function () {
            /* $('#test').val('dasda');*/
             if (window.confirm("确定删除?")) {
                 var obj = $(this).parent().parent();
                 $.post("<%=basePath%>users/information/del?id=" + $(this).attr("id"), function (data) {
                     if (data.msg === "ok") {
                         obj.remove();
                         alert("删除成功");
                     } else {
                         alert("权限不足");
                     }
                 });
             }
         });
         $(".compile").bind("click", function () {
             if (window.confirm("确定修改?")) {
                 location.replace("<%=basePath%>users/information/update/" + $(this).attr("id"));
             }
         });
         $(".page").click(function () {
             var c = $(this).attr("id");
             $("#page").attr("value", c);
             $("#selectform").submit();
         });
         $(window).keydown(function(event){
             if (event.keyCode===17){
                 $("#selectform").submit();
             }
         });
     });
 </script>
</body>
 </html>