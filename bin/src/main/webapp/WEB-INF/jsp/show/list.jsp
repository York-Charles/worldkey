
<%--@elvariable id="q" type="java.lang.String"--%>
<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo"--%>
<%--@elvariable id="oneType" type="com.worldkey.entity.OneType"--%>

<%--@elvariable id="threeTypes" type="java.util.List"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>展示管理页面</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div>
    <c:if test="${threeTypes.size()!=1}">
        <div style="height: 40px; line-height: 40px;float: left;">
            <c:forEach items="${threeTypes}" var="item">
                <c:if test="${item.id==threeType}">
                <button class="btn btn-sm btn-info navType" data-id="${item.id}">${item.typeName}</button>
                </c:if>
                <c:if test="${item.id!=threeType}">
                <button class="btn btn-sm navType" data-id="${item.id}">${item.typeName}</button>
                </c:if>
            </c:forEach>
        </div>
    </c:if>
    <form style="float: right;" class="form-inline">
        <input type="hidden" name="oneType" value="${oneType.id}">
        <input type="text" placeholder="search" value="${q}" name="q" id="q" class="form-control"
               style="width: 250px; line-height: 30px;margin-top: 5px;">
        <button type="submit" class="btn btn-info" style="display: inline-block;margin-top: 5px;" >搜索</button>
    </form>
</div>
<table class="table table-hover table-striped text-center">
    <tr>
        <td>*</td>
        <td>标题</td>
        <td>类型</td>
        <td>操作</td>
        <td>链接</td>
    </tr>
    <c:forEach items="${pageInfo.list}" var="item" varStatus="c">
        <tr>
           <td>${c.count}</td>  
           <td>
                <%-- 没有标题的展示 --%>
                <c:if test="${item.title==null}">
                    <a href="${item.webUrl}">无标题${item.id}</a>
                </c:if>  
                <%-- 有标题的展示 --%>
                <c:if test="${item.title!=null}">
                    <a href="${item.webUrl}">${item.title}</a>
                </c:if>  
            </td>
            <td>${oneType.typeName}<c:forEach items="${threeTypes}" var="typeItem"><c:if
                    test="${typeItem.id==item.type}">${typeItem.typeName}</c:if></c:forEach>
            </td>  
            <td>
                <button data-id="${item.id}" class="del btn-sm glyphicon glyphicon-trash btn btn-danger"></button>
                <button data-id="${item.id}" type="button" class="move btn-sm  btn btn-warning" data-toggle="modal"
                        data-target="#move"><c:if test="${item.showPush!=0}">【已】</c:if>推送
                </button>
            </td>
            <td>${item.webUrl}</td>
        </tr>
    </c:forEach>
</table>
<nav aria-label="Page navigation" class="text-center">
    <ul class="pagination">
        <c:if test="${!pageInfo.isFirstPage}">
            <li>
                <a class="page " data-id="${pageInfo.prePage}" aria-label="Previous">
                    <span>上一页</span> </a></li>
        </c:if>
        <%--@elvariable id="pageinfo" type="com.github.pagehelper.PageInfo"--%>
        <c:forEach
                begin="${pageInfo.pages<=10?1:(pageInfo.pageNum-5<1?1:(pageInfo.pageNum-5>pageInfo.pages-10?pageInfo.pages-10:pageInfo.pageNum-5))}"
                end="${pageInfo.pages<=10?pageInfo.pages:(pageInfo.pageNum+4>pageInfo.pages?pageInfo.pages:(pageInfo.pageNum+4<10?10:pageInfo.pageNum+4))}"
                var="i">
            <c:if test="${pageInfo.pageNum==i }">
                <li class="active">
                    <span class="page" data-id="${i}">${i}</span></li>
            </c:if>
            <c:if test="${pageInfo.pageNum!=i }">
                <li>
                    <a class="page" data-id="${i}"><span> ${i}</span></a></li>
            </c:if>
        </c:forEach>

        <c:if test="${!pageInfo.isLastPage}">
            <li>
                <a class="page" data-id="${pageInfo.nextPage}" aria-label="Next">
                    <span>下一页</span>
                </a></li>
        </c:if>

    </ul>
</nav>
<!-- 分页导航结束 -->
<%--推送的拟态框--%>
<!-- Modal -->
<div class="modal fade" id="move" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">展示推送</h4>
            </div>
            <div class="modal-body">
                <select id="selectOneType" name="oneType" title="">
                    <%--@elvariable id="oneTypes" type="java.util.List"--%>
                    <option value="0">请选择</option>
                    <c:forEach items="${oneTypes}" var="item">
                       <c:if test="${item.id!=33&&item.id!=34 }"><option value="${item.id}">${item.typeName}</option> </c:if>
                    </c:forEach>
                </select>
                <select name="type" id="type" title="">
                    <option value="0">请选择</option>
                </select>
                <select name="three" id="three" title="">
                    <option value="0">请选择</option>
                </select>
            </div>
            <div class="modal-footer">
                <button type="button" id="qx" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" id="push" class="btn btn-primary">确定</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    $('.page').css('cursor', 'pointer');
    /*id*/
    var itemID;
    var threeAjax;
    var itemButton;
    var threeType = "${threeType}";
    var q="${q}";
    $(".navType").click(function () {
        var threeTypeTemp = $(this).data("id");
        location.href = "/show/manageList?threeType=" + threeTypeTemp;
    });
    $(".page").click(function () {
        var pageNum = $(this).data("id");
        if (threeType !== "") {
            location.href = "/show/manageList?threeType=" + threeType + "&page=" + pageNum;
        }else {
            location.href = "/show/manageList?oneType=${oneType.id}&q="+q+"&page="+pageNum;
        }
    });


    /*移动*/
    $(".move").click(function () {
        itemID = $(this).data("id");
        itemButton = $(this);
    });

    $("#push").click(function () {
        var type = $("#three").val();
        window.console.log(three);
        var typeName;
        for (var i = 0; i < threeAjax.result.length; i++) {
            if (threeAjax.result[i].id == type) {
                typeName = threeAjax.result[i].typeName;
                break;
            }
        }
        if (confirm("推送到:" + typeName + "?")) {
            $.post("/show/push", {itemID: itemID, type: type}, function (data) {
                if (data.code === 200 && data.msg === "ok") {
                    $("#qx").click();
                    itemButton.html("【已】推送")
                } else {
                    alert(data.result);
                }
            })
        }
    });
  //4.18修改三级联动
    $("#selectOneType").change(function () {
    	
        $.post("/type/two/" + $(this).val(), function (date) {
            var two; 
            twoAjax = date;
            for (var i = 0; i < date.result.length; i++) {
                two += "<option value=" + date.result[i].id + " class=type" + ">" + date.result[i].typeName + "</option>";
            }
            $("#type").html(two);
            $.post("/type/three/" + $(".type").val(), function (date) {
                var three;
                threeAjax = date;
                for (var j = 0; j < date.result.length; j++) {
                    three += "<option value=" + date.result[j].id + " class=three" + ">" + date.result[j].typeName + "</option>";
                }
                $("#three").html(three);
            });
            
        });
    });
    $("#type").change(function () {
        $.post("/type/three/" + $(this).val(), function (date) {
            var three;
            threeAjax = date;
            for (var i = 0; i < date.result.length; i++) {
                three += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
            }
            $("#three").html(three);
        });
    }) 



/*    $("#selectOneType").change(function () {
        var str;
        $.post("type/two/" + $(this).val(), function (date) {
            twoAjax = date;
            for (var i = 0; i < date.result.length; i++) {
                str += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
            }
            $("#type").html(str);
        });
    });
*/
    /*删除*/
    $(".del").click(function () {
        var id = $(this).data("id");
        if (window.confirm("删除？")) {
            var obj = $(this).parent().parent();
            $.post("/informationall/del", {id: id}, function (data) {
                if (data.code === 200 && data.msg === 'ok') {
                    obj.remove();
                } else {
                    alert("删除失败");
                }
            })
        }
    });
</script>
</body>
</html>
