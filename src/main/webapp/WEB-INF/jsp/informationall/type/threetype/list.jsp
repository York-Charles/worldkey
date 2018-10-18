<%--@elvariable id="oneType" type="com.worldkey.entity.OneType"--%>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <script src="js/jquery-3.1.1.min.js"></script>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://code.jquery.com/jquery-migrate-1.0.0.js"></script>
    <link rel="stylesheet" type="text/css" href="css/manage1.css">
    <style type="text/css">
        .input {
            display: block;
            margin-bottom: 10px;
        }

        #addtype {
            text-align: center;
        }

        #addtype input {
            text-align: center;
            margin-bottom: 10px;
            font-size: 14px;
            border: 2px solid #dbb769;
            height: 30px;
            line-height: 30px;
            border-radius: 5px;
        }

        .name {
            line-height: 28px;
            border: 1px solid #ffffff;
            font-size: 14px;
        }

        #addtype {
            display: none;
            position: absolute;
            left: 30%;
            top: 40%;
        }

        .add {
            cursor: pointer;
        }

        body {
            width: 100%;
            position: relative;
        }

        .add > img {
            width: auto;
            height: auto;
        }
    </style>
    <title>三级分类列表</title>
</head>
<body>
<div class="input">
    <a class="add"> <span>添加</span><img alt="" src="icon/icon/icon_btn_add.png"></a>
</div>
<hr>
<table id="tb" class="table table-striped">
    <tr>
        <td>id</td>
        <td>序号</td>
        <td>名称</td>
        <td>操作</td>
         <td>审核</td>
       <!-- <td>排序</td>-->
    </tr>
    <%--@elvariable id="list" type="java.util.List"--%>
    <c:forEach items="${list }" var="c" varStatus="co">
        <tr>
            <td>${c.id}</td>
            <td>${co.count}</td>
            <td>
                <input value="${c.typeName}" readonly="readonly" class="name" title=""/></td>
                
            
                
            <td><span class="compile" id="${c.id}"> <img alt=""
                                                         src="icon/icon/icon_btn_write.png">
				</span> <span class="del" id="${c.id}"> <img alt=""
                                                             src="icon/icon/icon_btn_cancel.png">
				</span></td>
				
				    <td>
                    <select id="${c.id}"  class="checked form-control" title="">
                        <!-- 已审核 -->
                        
                        <c:if test="${c.checked==1}">
                            <option value="1" selected="selected">已审核</option>
                            <option value="0">未审核</option>
                        </c:if>
                        <!-- 未审核 -->
                        <c:if test="${c.checked==0}">
                            <option value="1">已审核</option>
                            <option value="0" selected="selected">未审核</option>
                        </c:if>

                    </select>
                </td>
                
                
            <!--  <td>关闭排序功能
                <c:if test="${!co.first}">
                    <button class="btn-info glyphicon glyphicon-arrow-up replace" data-id="${c.id}"
                            data-replace="${list.get(co.index-1).id}"></button>
                </c:if>
                <c:if test="${!co.last}">
                    <button class="btn-info glyphicon glyphicon-arrow-down replace" data-id="${c.id}"
                            data-replace="${list.get(co.index+1).id}"></button>
                </c:if>
            </td>
            -->
        </tr>
    </c:forEach>
</table>

<div id="addtype" class="input">
    <form action="type/three/add" method="post" target="new">
        <input type="hidden" name="twoType" value="${twoType}">
        <input name="typeName" placeholder="模块名"><br>
        <input type="submit" value="添加">
        <input type="button" id="hid" value="取消">
    </form>
</div>
<script>
    $(function () {
        $(".replace").click(function () {
        
            var id = $(this).data("id");
            var replace = $(this).data("replace");
           
            $.post("/type/replace3", {id: id, replace: replace}, function (data) {
                if (data === 1) {
                    location.href = "/type/three/list/${twoType}";
                } else {
                    alert(data);
                }
            });
        });
        $(".add").click(function () {
            $("#addtype").css("display", "block");
        });
        $("#hid").click(function () {
            $("#addtype").css("display", "none");
        });


        $("tr").mouseover(function () {
            $(this).children().eq(1).children("input").css("background-color", "lightgreen");
        });
        $("tr").mouseout(function () {
            $(this).children().eq(1).children("input").css("background-color", "#ffffff");
        });
        $(".del").click(function () {
            if (window.confirm("确定删除?")) {
                var obj = $(this).parent().parent();
                $.post('type/three/del/' + $(this).attr('id'), function (data) {
                    if (data.msg == "ok") {
                        obj.remove();
                        alert("删除成功");
                    } else {
                        alert(data.result);
                    }
                });
            }
        });

        var compileType = 1;
        $(".compile").click(function () {
            if (compileType === 1) {
                $(this).children().attr("src", "icon/icon/btn_save.png");
                $(this).parent().parent().children().eq(2).children("input").removeAttr("readonly");
                $(this).parent().parent().children().eq(1).children("input").css("border-color", "#e3e3e3");
                compileType = 2;
            } else {
                if (window.confirm("确定修改?")) {
                    var name = $(this).parent().prev().children("input").val();
                    $.post("type/three/update?id=" + $(this).attr('id') + "&typeName=" + name, function (data) {
                        if (data.msg !== "ok") {
                            alert(data.result);
                        }
                    });
                }
                $(this).children().attr("src", "icon/icon/icon_btn_write.png");
                $(this).parent().parent().children().eq(1).children("input").attr("readonly", "readonly");
                $(this).parent().parent().children().eq(1).children("input").css("border-color", "#ffffff");
                compileType = 1;
            }
        });
        
        $(".checked").change(function () {
            var checked = $(this).val();
            $.post('<%=basePath%>type/checked?id=' + $(this).attr('id') + '&checked=' + checked, function (date) {
                alert("操作成功");
            })
        });
        
    });
</script>
</body>
</html>