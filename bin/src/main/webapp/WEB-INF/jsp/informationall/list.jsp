<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<% String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>资讯</title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="js/jquery-3.1.1.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <title>资讯列表</title>
</head>
<body>
<%--@elvariable id="pageinfo" type="com.github.pagehelper.PageInfo"--%>
<%--@elvariable id="select" type="com.worldkey.entity.InformationExample"--%>
<%--@elvariable id="twotypes" type="java.util.List"--%>
<%--@elvariable id="onetypes" type="java.util.List"--%>
<%--@elvariable id="onetype" type="com.worldkey.entity.OneType"--%>
<sf:form action="informationall/list" cssClass="form-inline text-center" id="selectform" modelAttribute="select">
    <div class="form-group">
        <label>标题</label>
        <sf:input path="title" id="title" cssClass="form-control"/>
    </div>
   <div class="form-group">
        <label for="onetype" class="sr-only">1类型</label>
        <select name="onetype" id="onetype" class="form-control">
            <c:forEach items="${onetypes}" var="c">
                <c:if test="${c.id==onetype}">
                    <option value="${c.id}" selected="selected">${c.typeName}</option>
                </c:if>
                <c:if test="${c.id!=onetype}">
                    <option value="${c.id}">${c.typeName}</option>
                </c:if>
            </c:forEach>
        </select>
    </div>
    
    <div class="form-group">
        <label for="twotype" class="sr-only">2类型</label>
        <select name="twotype" id="twotype" class="form-control">
            <c:forEach items="${twotypes}" var="c2">
                <c:if test="${c2.id==twotype}">
                    <option value="${c2.id}" selected="selected">${c2.typeName}</option>
                </c:if>
                <c:if test="${c2.id!=twotype}">
                    <option value="${c2.id}">${c2.typeName}</option>
                </c:if>
            </c:forEach>
        </select>
    </div>
    <div class="form-group">
        <label for="threetype" class="sr-only">3类型</label>
        <select name="threetype" id="threetype" class="form-control">
            <c:forEach items="${threetypes}" var="c3">
                <c:if test="${c3.id==threetype}">
                    <option value="${c3.id}" selected="selected">${c3.typeName}</option>
                </c:if>
                <c:if test="${c3.id!=threetype}">
                    <option value="${c3.id}">${c3.typeName}</option>
                </c:if>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label>状态<select name="checked" class="form-control">
            <option value="-1" <c:if test="${select.checked==-1}">selected="selected" </c:if>>不限</option>
            <option value="1" <c:if test="${select.checked==1}">selected="selected" </c:if> id="stickAppear">已审核</option>
            <option value="0" <c:if test="${select.checked==0}">selected="selected" </c:if>>未审核</option>
            <option value="3" <c:if test="${select.checked==3}">selected="selected" </c:if>>已下架</option>
        </select>
        </label></div>
    <input type="hidden" name="page" value="${pageinfo.pageNum}" id="page">
    <input type="hidden" name="pagesize" value="${pageinfo.pageSize}" id="pagesize">
    <button class=" btn btn-sm btn-warning glyphicon glyphicon-search ">查询</button>
    <%--<a href="informationall/toadd" class="btn btn-sm btn-warning  glyphicon glyphicon-plus" target="new">添加</a>--%>
</sf:form>
<table id="tb" class="table table-hover text-center table-condensed">
    <thead>
    <tr>
        <th class="text-center">*</th>
        <th class="text-center">标题</th>
      <!--  <th class="text-center">类型</th> -->  
        <th class="text-center">操作</th>
        <th class="text-center">审核</th>
        <!-- <th class="text-center">置顶</th> -->
        <th class="text-center">链接</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${pageinfo.list }" var="c" varStatus="co">
        <c:if test="${c.checked!=2}">
            <tr>
                <td>${co.count}</td>
                <td><a href="${c.weburl}" target="new">${c.title}</a></td>
      
                <td>   
             <!--    -->  <button id="${c.id}" class="compile btn-sm glyphicon glyphicon-edit btn btn-warning"></button>
                    <button id="${c.id}" class="del btn-sm glyphicon glyphicon-trash btn btn-danger"></button>
                    <button type="button" data-id="${c.id}" class="change btn-sm  btn btn-warning" data-toggle="modal"
                            data-target="#typeChange">
                        移动
                    </button>
                    <button data-id="${c.id}" type="button" class="move btn-sm  btn btn-warning" data-toggle="modal"
                        data-target="#move"><c:if test="${c.showPush!=0}"></c:if>精选
                </button>
          
                </td>
                <td>
                    <select id="${c.id}" class="checked form-control" title="">
                        <!-- 已审核 -->
                        <c:if test="${c.checked==1}">
                            <option value="1" selected="selected">已审核</option>
                            <option value="0">未审核</option>
                            <option value="3">已下架</option>
                        </c:if>
                        <!-- 未审核 -->
                        <c:if test="${c.checked==0}">
                            <option value="1">已审核</option>
                            <option value="0" selected="selected">未审核</option>
                            <option value="3">已下架</option>
                        </c:if>
                        <!-- 已下架 -->
                        <c:if test="${c.checked==3}">
                            <option value="1">已审核</option>
                            <option value="0">未审核</option>
                            <option value="3" selected="selected">已下架</option>
                        </c:if>

                    </select>
                </td>
                <!--  <td>
                	 <input type="button" data-id="${c.id}" class="btn btn-warning stick" value="置顶">   
                </td>
                -->
                <td>${c.weburl}</td>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>
<!-- 修改类型的 Modal -->
<div class="modal fade" id="typeChange" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改记录的分类</h4>
            </div>
            <div class="modal-body">
                <sf:form action="informationall/list" cssClass="form-inline text-center" id="selectform"
                         modelAttribute="select">
                    <div class="form-group">
                        <label for="onetype" class="sr-only">1类型</label>
                        <select name="onetype" id="changeOneType" class="form-control" title="">
                            <c:forEach items="${onetypes}" var="c">
                              <c:if test="${c.id!=27&&c.id!=33&&item.id!=34 }"> <option class="changeOneTypeOption" value="${c.id}">${c.typeName}</option></c:if> 
                            </c:forEach>
                        </select>
                    </div>
                   <div class="form-group">
                        <label for="type" class="sr-only">2类型</label>
                        <select name="" id="changeTwoType" class="form-control" title="">
                            <c:forEach items="${twotypes}" var="c">
                                <c:if test="${c.typeName!='不限'}">
                                <option class="changeTwoTypeOption" value="${c.id}">${c.typeName}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                      <div class="form-group">
                        <label for="three" class="sr-only">3类型</label>
                        <select name="three" id="changeThreeType" class="form-control" title="">
                            <c:forEach items="${threetypes}" var="c">
                                <c:if test="${c.typeName!='不限'}">
                                <option class="changeTwoTypeOption" value="${c.id}">${c.typeName}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    
                    
                    
                </sf:form>
            </div>
            <div class="modal-footer">
                <button type="button" id="dismiss" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="saveChange">确定</button>
            </div>
        </div>
    </div>
</div>
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
                    <c:forEach items="${onetypes}" var="item">
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

<%@include file="../fragment/page.jsp" %>
<script>
    $(function () {
        var changElement;
        var changeId;
        $('.change').click(function () {
            changeId = $(this).data("id");
            changElement=$(this);
        });        
        $("#saveChange").click(function () {
            var changeOneTypeID=$('#changeOneType').val();
            var changeTwoType=$('#changeTwoType').val();
            var changeThreeType=$('#changeThreeType').val();
            var changeOneTypeOptionText=$('.changeOneTypeOption[value='+changeOneTypeID+']').text();
            var changeTwoTypeOptionText=$('.changeTwoTypeOption[value='+changeTwoType+']').text();       
            var changeThreeTypeOptionText=$('.changeThreeTypeOption[value='+changeThreeType+']').text();
            var s=changeOneTypeOptionText;//+'->'+changeTwoTypeOptionText+'->'+changeThreeTypeOptionText;
          /*  alert(s);*/
            /*改变类型的请求操作*/
            /*var changeTwoTypeText=$('#changeTwoType').text();*/
           $.post('/informationall/changeThreeType',{changeThreeType:changeThreeType,changeId:changeId},function (data) {
                if (200 === data.code){
                    changElement.parent().parent().children().eq(2).text(s);
                    $("#dismiss").click();
                }
            })
        });
    	/*2018.4.19因添加三级标签，将其注掉
        $("#saveChange").click(function () {
            var changeOneTypeID=$('#changeOneType').val();
            var changeTwoType=$('#changeTwoType').val();
            var changeOneTypeOptionText=$('.changeOneTypeOption[value='+changeOneTypeID+']').text();
            var changeTwoTypeOptionText=$('.changeTwoTypeOption[value='+changeTwoType+']').text();
            var s=changeOneTypeOptionText+'->'+changeTwoTypeOptionText;
          //alert(s);
            //改变类型的请求操作
           //var changeTwoTypeText=$('#changeTwoType').text();
           $.post('/informationall/changeTwoType',{changeTwoType:changeTwoType,changeId:changeId},function (data) {
                if (200 === data.code){
                    changElement.parent().parent().children().eq(2).text(s);
                    $("#dismiss").click();
                }
            })
        });
        $("#changeOneType").change(function () {
            var str;
            $.post("type/two/" + $(this).val(), function (date) {
                for (var i = 0; i < date.result.length; i++) {
                    str += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
                }
                $("#changeTwoType").html(str);
            });
        });*/

        /*$("#zhiding").click(function(){
        	var id = $(".cid").val();
        	$.post("informationall/zhiding",{id:id},function(data){
        		var code = data.code;
				if (code === 200) {
        			alert(data.result);
        		}else{
        			alert("未知错误!");
        		}
        	});
        });
        $.post("/informationall/zhiding",{id:id},function(data){
		var code = data.code;
		if (code === 200) {
			alert(data.result);
		}else{
			alert("未知错误!");
		}
	});*/
        $('.stick').click(function(){
        	var id = $(this).data("id");
        	$.ajax({
        		url:"/informationall/zhiding",
        		type:'POST',
        		async:'true',
        		cache:"false",
        		data:{id:id},
        		success: function (data) {
        			var code = data.code;
        			if (code === 200) {
        				alert(data.result);
        				location.reload();
        			}else{
        				alert("未知错误!");
        			}
                }
        	});
        });
        
        $("#changeOneType").change(function () {
        	
            $.post("/type/two/" + $(this).val(), function (date) {
                var two; 
                for (var i = 0; i < date.result.length; i++) {
                    two += "<option value=" + date.result[i].id + " class=changeTwoType" + ">" + date.result[i].typeName + "</option>";
                }
                $("#changeTwoType").html(two);
                $.post("/type/three/" + $(".changeTwoType").val(), function (date) {
                    var three = "";
                    for (var j = 0; j < date.result.length; j++) {
                        three += "<option value=" + date.result[j].id + " class=changeThreeType" + ">" + date.result[j].typeName + "</option>";
                    }
                    $("#changeThreeType").html(three);
                });
                
            });
        });
        $("#changeTwoType").change(function () {
            $.post("/type/three/" + $(this).val(), function (date) {
                var three = "";
                for (var i = 0; i < date.result.length; i++) {
                    three += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
                }
                $("#changeThreeType").html(three);
            });
        })       
        $(".checked").change(function () {
            var checked = $(this).val();
            $.post('<%=basePath%>informationall/checked?id=' + $(this).attr('id') + '&checked=' + checked, function (date) {
                alert(date.msg);
            })
        });
        $(".del").click(function () {
            if (window.confirm("确定删除?")) {
                var obj = $(this).parent().parent();
                $.post("<%=basePath%>informationall/del?id=" + $(this).attr("id"), function (data) {
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
                location.replace("<%=basePath%>informationall/toupdate/+" + $(this).attr("id"));
            }
        });
        $(".page").click(function () {
            var num = "${pageinfo.pageNum}";
            var c = $(this).attr("id");
            if (c === num) {
                return;
            }
            $('#page').attr("value", c);
            $('#selectform').submit();
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
        
	$("#onetype").change(function () {   
		    var one = $(this).val();
            $.post("/type/two/" + $(this).val(), function (date) {
            	var two = "";
                for (var i = 0; i < date.result.length; i++) {
                    two += "<option value=" + date.result[i].id + " class=twotype"  + ">" + date.result[i].typeName + "</option>";
                }
                $("#twotype").html(two);
                $.post("/type/three/" + $(".twotype").val(), function (date) {
                	var three = "";
                    for (var j = 0; j < date.result.length; j++) {
                        three += "<option value=" + date.result[j].id + " class=threetype" + ">" + date.result[j].typeName + "</option>";
                    }
                    $("#threetype").html(three);
                });
                
            });
        });
       $("#twotype").change(function () {
            $.post("/type/three/" + $(this).val(), function (date) {
            	var three = "";
                for (var i = 0; i < date.result.length; i++) {
                    three += "<option value=" + date.result[i].id + " class=threetype" + ">" + date.result[i].typeName + "</option>";
                }
                $("#threetype").html(three);
            });
        }) 
        /*原始代码2018.4.19注
        $("#onetype").change(function () {
            var one = $(this).val();
            $.post("type/two/" + $(this).val(), function (date) {
                var two = "<option value=0>不限</option>";
                for (var i = 0; i < date.result.length; i++) {
                    two += "<option value=" + date.result[i].id + ">" + date.result[i].typeName + "</option>";
                }
                $("#type").html(two);
            });
        });
        */
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
                three += "<option value=" + date.result[i].id + " class=three " +">" + date.result[i].typeName + "</option>";
            }
            $("#three").html(three);
        });
    }) 
    
</script>
</body>
</html>